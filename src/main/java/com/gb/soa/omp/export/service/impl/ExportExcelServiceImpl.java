/*
 * PROJECT NAME: omp_cexport
 * PACKAGE NAME: com.gb.soa.omp.export.service.impl
 * FILE    NAME: ExportExcelServiceImpl.java
 * COPYRIGHT: Copyright(c) © 2016 Goodbaby Company Ltd. All Rights Reserved
 */
package com.gb.soa.omp.export.service.impl;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.gb.soa.omp.ccommon.api.exception.BusinessException;
import com.gb.soa.omp.ccommon.api.exception.ExceptionType;
import com.gb.soa.omp.ccommon.datasource.DataSourceContextHolder;
import com.gb.soa.omp.ccommon.util.*;
import com.gb.soa.omp.exchange.model.CommonQuery;
import com.gb.soa.omp.exchange.model.ExcuteSqlResultModel;
import com.gb.soa.omp.export.api.model.ExcelInput;
import com.gb.soa.omp.export.api.request.*;
import com.gb.soa.omp.export.api.response.*;
import com.gb.soa.omp.export.api.service.ExportDataService;
import com.gb.soa.omp.export.api.service.ExportExcelService;
import com.gb.soa.omp.export.api.util.CommonQueryClientUtil;
import com.gb.soa.omp.export.dao.CommonQueryDao;
import com.gb.soa.omp.export.dao.ExcelInputDao;
import com.gb.soa.omp.export.dao.ExcelInputHdrDao;
import com.gb.soa.omp.export.entity.EXCEL_INPUT;
import com.gb.soa.omp.export.entity.EXCEL_INPUT_HDR;
import com.gb.soa.omp.export.service.CommonJsonQueryService;
import com.gb.soa.omp.export.service.model.BatchExcuteModel;
import com.gb.soa.omp.export.service.model.ExcelInputImportArgs;
import com.gb.soa.omp.export.service.model.MatchesInfoAndKeyValue;
import com.gb.soa.omp.export.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 *
 */
@Service("exportExcelService")
public class ExportExcelServiceImpl implements ExportExcelService {

    private static final Logger log = LoggerFactory.getLogger(ExportDataServiceImpl.class);

    private static JsonMapper mapper;

    static {
        mapper = new JsonMapper();
        mapper.getMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @Value("${excel.host}")
    private String excelHost;

    @Value("${excel.path}")
    private String excelPath;

    @Resource(name = "exportCommonJsonQueryService")
    private CommonJsonQueryService commonJsonQueryService;

    @Resource(name = "exportDataService")
    private ExportDataService exportDataService;

    @Resource(name = "commonQueryDao")
    private CommonQueryDao commonQueryDao;

    @Resource
    private ExcelInputDao excelInputDao;
    @Resource
    private ExcelInputHdrDao excelInputHdrDao;

    @Resource(name = "commonQueryDyTransactionManager")
    private DataSourceTransactionManager transactionManager;

    /**
     * excel文件根目录
     */
    @Value("${minio.excel.bucketName}")
    private String bucketName;

    @Value("${excel.child.path}")
    private String excelChildPath;

    @Resource
    private MinioClientUtil minioClientUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static Gson gson = new GsonBuilder().create();

    @Resource(name = "commonQueryDyTransactionManager")
    private PlatformTransactionManager dynamicTransactionManager;

    /**
     *
     */
    @Override
    public ExcelExportResponse exportExcel(ExcelExportRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("begin exportExcel request:{}", mapper.toJson(request));
        }
        ExcelExportResponse response = new ExcelExportResponse();
        TransactionStatus status = null;
        try {
            request.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10033);
            Long batchNo = request.getBatchNo();
            String sheetId = request.getSheetName();
            List<Object[]> argsList = new ArrayList<Object[]>();
            List<ArrayList<String>> excelDataList = request.getExcelDataList();
            for (int i = 0; i < excelDataList.size(); i++) {
                ArrayList<String> list = excelDataList.get(i);
                Object[] param = new Object[36];
                param[0] = SeqUtil.getSeq(SeqUtil.EXCEL_INPUT_SERIES);
                param[1] = batchNo;
                param[2] = sheetId;
                param[3] = i + 1;
                param[4] = 1L;
                param[5] = 1L;
                param[6] = "N";
                param[7] = request.getTenantNumId();
                param[8] = request.getDataSign();
                param[9] = 0L;
                for (int j = 0; j < list.size(); j++) {
                    String str = list.get(j);
                    if (isNumeric(str)) {
                        str = removeUnnecessarZero(str);
                    }
                    ;
                    param[10 + j] = str;
                }
                argsList.add(param);
            }
            List<Object[]> insertArgs = null;

            status = transactionManager.getTransaction(TransactionUtil.newTransactionDefinition());
            int insertCount = argsList.size() % 10000 == 0 ? argsList.size() / 10000 : (argsList.size() / 10000 + 1);
            for (int i = 0; i < insertCount; i++) {
                if (i != insertCount - 1) {
                    insertArgs = argsList.subList(i * 10000, (i + 1) * 10000);
                } else {
                    insertArgs = argsList.subList(i * 10000, argsList.size());
                }
                excelInputDao.batchInsertEntityNew(insertArgs);
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            if (status != null) {
                transactionManager.rollback(status);
            }
            ExceptionUtil.processException(e, response);
        }
        if (log.isDebugEnabled()) {
            log.debug("end exportExcel  return:{}", response.toLowerCaseJson());
        }
        return response;
    }

    // 是否数字
    public static boolean isNumeric(String str) {
        if (str.matches("\\d+\\.\\d+")) {
            return true;
        }
        return false;
    }

    // 去掉数字后面的多余的0 比如2.0-〉2
    public String removeUnnecessarZero(String str) {
        String pointNum = str.replaceAll("\\d+\\.", "");
        if (Long.valueOf(pointNum) == 0) {
            str = str.replaceFirst("\\.\\d+", "");
            return str;
        }
        return str;
    }

    @Override
    public ExelInputsQueryResponse queryExelInputs(ExelInputsQueryRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("begin queryExelInputs request:{}", mapper.toJson(request));
        }
        ExelInputsQueryResponse response = new ExelInputsQueryResponse();
        try {
            request.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10033);
            int pageSize = request.getPageSize();
            if (request.getPageSize() != 0) {
                int totalCount = excelInputDao.getCountBySheetId(request.getTenantNumId(), request.getDataSign(),
                        request.getBatchNo(), request.getSheetId());
                int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize + 1);
                response.setTotalPage(totalPage);
            }
            List<ExcelInput> ecxelList = excelInputDao.getExelInputs(request.getTenantNumId(), request.getDataSign(),
                    request.getBatchNo(), request.getSheetId(), (request.getPageNum() - 1) * pageSize, pageSize);
            response.setEcxelList(ecxelList);
        } catch (Exception e) {
            ExceptionUtil.processException(e, response);
        }
        if (log.isDebugEnabled()) {
            log.debug("end queryExelInputs  return:{}", response.toLowerCaseJson());
        }
        return response;
    }

    @Override
    public ExcelInputHdrInsertResponse insertExcelInputHdr(ExcelInputHdrInsertRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("begin insertExcelInputHdr request:{}", mapper.toJson(request));
        }
        ExcelInputHdrInsertResponse response = new ExcelInputHdrInsertResponse();
        try {
            request.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10033);
            EXCEL_INPUT_HDR hdr = new EXCEL_INPUT_HDR();
            hdr.setINPUT_NUM_ID(request.getInputNumId());// '导入编码',
            hdr.setFILE_NAME(request.getFileName());// '文件名自动加时分秒后缀',
            hdr.setFILE_PATH(request.getFilePath());// '文件路径',
            hdr.setDOWNLOAD_QTY(request.getDownloadQty());// '下载次数',
            hdr.setTOTAL_ROWS(request.getTotalRows());// '文件记录数',
            hdr.setIMPORT_ROWS(request.getImportRows());// '成功导入记录数',
            hdr.setFAIL_ROWS(request.getFailRows());// '失败记录数',
            hdr.setWARNING_ROWS(request.getWarningRows());// '警告记录数',
            hdr.setSERIES(SeqUtil.getSeq(SeqUtil.EXCEL_INPUT_HDR_SERIES));
            hdr.setBATCH_NO(request.getBatchNo());
            hdr.setTENANT_NUM_ID(request.getTenantNumId());
            hdr.setDATA_SIGN(request.getDataSign());
            hdr.setFAIL_CAUSE(request.getFailCause());
            hdr.setEXCEL_TYPE(request.getExcelType());
            excelInputHdrDao.commonInsert(hdr);
        } catch (Exception e) {
            ExceptionUtil.processException(e, response);
        }
        if (log.isDebugEnabled()) {
            log.debug("end insertExcelInputHdr  return:{}", response.toLowerCaseJson());
        }
        return response;
    }

    /**
     *
     */
    @Override
    public InputBySeriesUpdateResponse updateInputBySeries(InputBySeriesUpdateRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("begin updateInputBySeries request:{}", mapper.toJson(request));
        }
        InputBySeriesUpdateResponse response = new InputBySeriesUpdateResponse();
        try {
            request.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10033);
            excelInputDao.updateInputBySeries(request.getSql());
        } catch (Exception e) {
            ExceptionUtil.processException(e, response);
        }
        if (log.isDebugEnabled()) {
            log.debug("end updateInputBySeries  return:{}", response.toLowerCaseJson());
        }
        return response;
    }

    /**
     * 导出Excel文件(csv格式)
     */
    @Override
    public ExcelFileExportResponse exportExcelFile(ExcelFileExportRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("begin exportExcelFile request:{}", mapper.toJson(request));
        }
        Long start = System.currentTimeMillis();
        ExcelFileExportResponse response = new ExcelFileExportResponse();
        try {
            request.validate(Constant.SUB_SYSTEM, ExceptionType.VCE10033);

            // 获取通用查询数据
            CommonQuery commonQuery = commonQueryDao.getModelWithTenant(request.getSqlId(), request.getTenantNumId(),
                    request.getDataSign());
            if (commonQuery == null) {
                commonQuery = commonQueryDao.getModelNoTenant(request.getSqlId(), request.getDataSign());
            }
            if (commonQuery == null) {
                throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40203,
                        "sqlId:" + request.getSqlId() + "没有获取到通用查询信息");
            }
            commonQuery.setPageSize(request.getPageSize());

            // 获取Excel表头
            String strExcelColumn = commonQuery.getExcelColumn();
            String[] heads = new String[]{};
            if (StringUtil.isNotNullOrBlankTrim(strExcelColumn)) {
                heads = strExcelColumn.split(",");
            }

            if (request.getInputParam() == null) {
                request.setInputParam(new HashMap<String, Object>());
            }
            request.getInputParam().put("tenant_num_id", request.getTenantNumId());
            request.getInputParam().put("data_sign", request.getDataSign());
            JSONObject inputParam = new JSONObject();
            inputParam.putAll(request.getInputParam());

            ExcuteSqlResultModel resultModel = new ExcuteSqlResultModel();
            resultModel = commonJsonQueryService.excuteSqlById(1l, "", commonQuery, inputParam,
                    request.getTenantNumId(), request.getDataSign());
            if (commonQuery.getNoDataException().equals("Y") && resultModel.getRecordCount() < 1L) {
                throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40203,
                        "通过sqlId:" + request.getSqlId() + "没有查询到记录");
            }
            if (log.isDebugEnabled()) {
                log.info("commonQuery:" + mapper.toJson(resultModel));
            }
            Long pageCount = resultModel.getPageCount();
            Long recordCount = resultModel.getRecordCount();
            if (log.isDebugEnabled()) {
                log.info("rowCount=:" + recordCount);
            }
            if (pageCount == 1) {
                List<Map<String, Object>> jsonList = resultModel.getData();
                String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".csv";
                String filePath = excelPath + "/" + fileName;
                buildCSVFile(heads, jsonList, filePath);
                response.setUrl(excelHost + fileName);
            } else {
                // 存储生成的文件路径
                List<String> fileList = new ArrayList<String>();

                // 先导出第一页数据
                List<Map<String, Object>> jsonList = resultModel.getData();
                String filePath = excelPath + "/" + UUID.randomUUID().toString().replaceAll("-", "") + ".csv";
                buildCSVFile(heads, jsonList, filePath);
                fileList.add(filePath);


                ExecutorService executorService = Executors.newFixedThreadPool(10);
                List<Future<Integer>> results = new ArrayList<Future<Integer>>();
                final String[] headsTemp = heads;
                for (Long i = 2l; i <= pageCount; i++) {
                    final long page = i;
                    final String filePath2 = excelPath + "/" + UUID.randomUUID().toString().replaceAll("-", "")
                            + ".csv";
                    fileList.add(filePath2);
                    final CommonQuery commonQueryTemp = commonQuery;

                    Future<Integer> future = executorService.submit(new Callable<Integer>() {
                        @Override
                        public Integer call() throws Exception {
                            return exportCsvFile(request, page, commonQueryTemp, inputParam, headsTemp, filePath2);
                        }
                    });
                    results.add(future);
                }
                executorService.shutdown();

                Boolean futureStatus = true;
                for (Future<Integer> future : results) {
                    if ("0".equals(future.get().toString())) {
                        futureStatus = false;
                    }
                }
                if (!futureStatus) {
                    try {
                        ZipCompressor zc = new ZipCompressor();
                        String[] fileArray = new String[fileList.size()];
                        fileList.toArray(fileArray);
                        zc.deleteFile(fileArray);
                    } catch (Exception e) {
                    }
                    throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40203, "多线程创建csv文件失败");
                }

                if (fileList.size() > 1) {
                    try {
                        // 将多个文件生成压缩包
                        String zipName = UUID.randomUUID().toString().replaceAll("-", "") + ".zip";
                        String zipPath = excelPath + "/" + zipName;
                        ZipCompressor zc = new ZipCompressor(zipPath);
                        String[] fileArray = new String[fileList.size()];
                        fileList.toArray(fileArray);
                        zc.compress(fileArray);
                        // 删除原来的csv文件
                        zc.deleteFile(fileArray);
                        // 返回压缩包路径
                        response.setUrl(excelHost + zipName);
                    } catch (Exception e) {
                        log.error("压缩、删除csv文件时异常:{}", e.getMessage(), e);
                    }
                }
            }

            Long end = System.currentTimeMillis() - start;
            String mes = "执行 exportExcelFile 耗时：" + end + " 毫秒...";
            log.info(mes);
            response.setMessage(mes);
        } catch (Exception e) {
            ExceptionUtil.processException(e, response);
        }
        if (log.isDebugEnabled()) {
            log.debug("end exportExcelFile  return:{}", response.toLowerCaseJson());
        }
        return response;
    }

    private Integer exportCsvFile(ExcelFileExportRequest request, Long currentPage, CommonQuery commonQuery,
                                  JSONObject inputParam, String[] heads, String filePath) {
        Integer result = 1;
        try {
            ExcuteSqlResultModel resultModel = new ExcuteSqlResultModel();

            resultModel = commonJsonQueryService.excuteSqlById(currentPage, "", commonQuery, inputParam,
                    request.getTenantNumId(), request.getDataSign());
            if (resultModel.getRecordCount() > 0) {

                List<Map<String, Object>> jsonList = resultModel.getData();
                buildCSVFile(heads, jsonList, filePath);
            }
        } catch (Exception e) {
            result = 0;
            log.error("当前页:" + currentPage + "由线程=" + Thread.currentThread().getName() + "执行失败:" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private class exportThread implements Runnable {

        private CountDownLatch countDownLatch;
        private String[] heads;
        private String filePath;
        private Long currentPage;
        private CommonQuery commonQuery;
        private JSONObject inputParam;
        private Long tenantNumId;
        private Long dataSign;

        public exportThread(CountDownLatch countDownLatch, ExcelFileExportRequest request, Long currentPage,
                            CommonQuery commonQuery, JSONObject inputParam, String[] heads, String filePath) {
            this.countDownLatch = countDownLatch;
            this.heads = heads;
            this.filePath = filePath;
            this.currentPage = currentPage;
            this.commonQuery = commonQuery;
            this.inputParam = inputParam;
            this.tenantNumId = request.getTenantNumId();
            this.dataSign = request.getDataSign();
        }

        @Override
        public void run() {
            ExcuteSqlResultModel resultModel = new ExcuteSqlResultModel();

            resultModel = commonJsonQueryService.excuteSqlById(currentPage, "", commonQuery, inputParam, tenantNumId,
                    dataSign);
            if (resultModel.getRecordCount() > 0) {
                try {
                    List<Map<String, Object>> jsonList = resultModel.getData();
                    buildCSVFile(heads, jsonList, filePath);
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void buildCSVFile(String[] heads, List<Map<String, Object>> list, String filePath) throws Exception {

        FileOutputStream fos = new FileOutputStream(filePath);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");

        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(heads);
        CSVPrinter csvPrinter = new CSVPrinter(osw, csvFormat);
        try {
            Map<String, Object> map;

            for (int i = 0; i < list.size(); i++) {
                map = list.get(i);
                csvPrinter.printRecord(map.values());
            }
        } catch (Exception e) {
            log.error("创建csv文件异常:{}" + e.getMessage(), e);
            throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40203, "创建csv文件异常" + e.getMessage());
        } finally {
            csvPrinter.flush();
            csvPrinter.close();
        }
    }

    /**
     * 根据通用查询获取数据生成EXCEL并返回文件URL
     */
    @Override
    public ExcelDownLoadResponse downLoadExcel(ExcelDownLoadRequest request) {

        if (log.isDebugEnabled()) {
            log.debug("beigin downLoadExcel request:{}", mapper.toJson(request));
        }
        ExcelDownLoadResponse response = new ExcelDownLoadResponse();
        List<Map<String, Object>> list = null;
        Long exportSeries = request.getExportSeries();
        Long tenantNumId = request.getTenantNumId();
        Long dataSign = request.getDataSign();
        try {

            String sqlId = request.getSqlId();
            if (StringUtil.isNotNullOrBlankTrim(sqlId)) {
                log.debug("进入通用查询**************" + sqlId);
                CommonExcuteBySqlIdRequest comSqlIdRequest = new CommonExcuteBySqlIdRequest();
                comSqlIdRequest.setTenantNumId(request.getTenantNumId());
                comSqlIdRequest.setDataSign(request.getDataSign());
                comSqlIdRequest.setSqlId(request.getSqlId());
                comSqlIdRequest.setInputParam(request.getInputParam());
                comSqlIdRequest.setPageNum(request.getPageNum());
                comSqlIdRequest.setPageSize(request.getPageSize());
                CommonExcuteBySqlIdResponse comSqlIdResponse = exportDataService.commonExcuteBySqlId(comSqlIdRequest);
                if (comSqlIdResponse.getCode() == 0) {
                    list = comSqlIdResponse.getResults();
                    if (list == null || list.size() == 0) {
                        response.setCode(-1);
                        response.setMessage("调用通用查询返回结果集为空");
                        return response;
                    }
                } else {
                    response.setCode(comSqlIdResponse.getCode());
                    response.setMessage(comSqlIdResponse.getMessage());
                    response.setFullMessage(comSqlIdResponse.getFullMessage());
                    return response;
                }
            } else {
                list = request.getDataResults();
            }

            if (request.getTableHeads() == null || request.getTableHeads().size() == 0) {
                response.setCode(-1l);
                response.setMessage("Excel表头不能为空");
                return response;
            }

            if (null != list && list.size() > 0) {
                Map<String, Object> tableHeads = request.getTableHeads();
                List<String> columnNames = new ArrayList<String>();
                List<List<String>> dataList = new ArrayList<List<String>>();
                boolean isFirst = true;
                for (Map<String, Object> map : list) {
                    List<String> data = new ArrayList<String>();

                    for (Map.Entry<String, Object> entry : tableHeads.entrySet()) {
                        if (isFirst) {
                            columnNames.add(String.valueOf(entry.getValue()));// 存入Excel表头的中文
                        }
                        // 获取Excel表头对应的值
                        String v = map.get(entry.getKey()) == null ? "" : String.valueOf(map.get(entry.getKey()));
                        data.add(v);
                    }
                    isFirst = false;
                    dataList.add(data);
                }

                ExcelDownLoadResponse excelDownLoadResponse = createPlatWarnExcelAndUpload(columnNames, dataList);
                ExceptionUtil.checkDubboException(excelDownLoadResponse);
                response.setUrl(excelDownLoadResponse.getUrl());
                if (null != exportSeries && exportSeries > 0) {
                    //更新
                    CommonQueryClientUtil.commonQueryForObject(tenantNumId, dataSign, "export_file_history-update", "series", exportSeries, "status_num_id", 1, "file_path", response.getUrl(), "last_updtme", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"), Integer.class);
                }
            }
        } catch (Exception ex) {
            if (null != exportSeries && exportSeries > 0) {
                //更新状态为失败
                CommonQueryClientUtil.commonQueryForObject(tenantNumId, dataSign, "export_file_history-update", "series", exportSeries, "status_num_id", 2, "last_updtme", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"), Integer.class);
            }
            ExceptionUtil.processException(ex, response);
        }
        if (log.isDebugEnabled()) {
            log.debug("end downLoadExcel response:{}", response.toLowerCaseJson());
        }
        return response;

    }


    private ExcelDownLoadResponse createPlatWarnExcelAndUpload(List<String> columnNames, List<List<String>> dataList) {
        ExcelDownLoadResponse response = new ExcelDownLoadResponse();
        try {
            // 创建工作簿
            Workbook workbook = new SXSSFWorkbook();
            // 创建sheet页
            Sheet sheet = workbook.createSheet("Sheet1");

            // 第一行单元格样式
            CellStyle cellStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setFont(font);
            // 其他行单元格样式
            CellStyle otherStyle = workbook.createCellStyle();
            otherStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            otherStyle.setAlignment(HorizontalAlignment.CENTER);
            otherStyle.setWrapText(true);
            // 第一行
            Row row = sheet.createRow(0);
            // 第一行第一列并填入值
            Cell c = row.createCell(0);
            c.setCellValue("行号");
            c.setCellStyle(cellStyle);
            // 设置第一列宽
            sheet.setColumnWidth(0, 1400);
            // 第一行其他列
            for (int i = 0; i < columnNames.size(); i++) {
                Cell cell = row.createCell(i + 1);
                cell.setCellValue(new XSSFRichTextString(columnNames.get(i)));
                cell.setCellStyle(cellStyle);
                // 列宽
                sheet.setColumnWidth(i + 1, columnNames.get(i).getBytes().length * 2 * 256);
            }
            // 其他行和列
            for (int i = 0; i < dataList.size(); i++) {
                Row otherRow = sheet.createRow(i + 1);
                otherRow.createCell(0).setCellValue(i + 1);
                List<String> datas = dataList.get(i);
                for (int j = 0; j < datas.size(); j++) {
                    Cell cell = otherRow.createCell(j + 1);
                    cell.setCellValue(new XSSFRichTextString(datas.get(j)));
                    cell.setCellStyle(otherStyle);
                }
            }
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".xls";
            String filePath = excelPath + "/" + fileName;
            response.setUrl(excelHost + fileName);
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();

            fileName = excelChildPath + fileName;
            JSONObject res = minioClientUtil.uploadFile(filePath, fileName, bucketName);
            Integer code = (Integer) res.get("code");
            String msg = (String) res.get("msg");
            if (Objects.nonNull(code) && code.equals(0)) {
                response.setUrl(msg);//成功,返回url
            } else {
                throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40203, "上传文件异常:" + msg);
            }

            workbook.close();
            return response;
        } catch (Exception e) {
            log.info("生成Excel表格或上传表格失败");
            e.printStackTrace();
            response.setCode(-1);
            response.setMessage(e.getMessage());
            return response;
        }

    }


    @Override
    public ExcelDataImportResponse importExcelData(ExcelDataImportRequest request) {
        log.info("begin importExcelData request:{}", mapper.toJson(request));
        long start = System.currentTimeMillis();
        ExcelDataImportResponse response = new ExcelDataImportResponse();
        try {
            request.validate(ExportUtil.SUB_SYSTEM, ExceptionType.VCE10034);
            Long tenantNumId = request.getTenantNumId();
            Long dataSign = request.getDataSign();
            Integer count = excelInputDao.getDataCountByBatchNo(tenantNumId, dataSign, request.getBatchNo(), 0L);
            if (count == 0) {
                return response;
            }
//			int pageSize = 5000;
//			int excuteCount = count%pageSize ==0?count/pageSize:(count/pageSize+1);
            Map<String, Object> dynamicParams = request.getDynamicParams();
            CommonQuery comquery = commonQueryDao.getNoTenantModel(request.getSqlId(), dataSign);
//			for (int i = 0; i < excuteCount; i++) {
//				int beginIndex = i*pageSize;
//				Runnable task = new Runnable() {
//					@Override
//					public void run() {
            TransactionStatus status = null;
            try {
                //存放所有缓存，（redis Mget提升缓存效率）
                final String[] sqlIdArr;
                if (comquery.getSubSqlId() == null) {
                    sqlIdArr = new String[]{};
                } else {
                    sqlIdArr = comquery.getSubSqlId().split(",");
                }
                Map<String, String> tempCacheMap = new ConcurrentHashMap<String, String>(count * sqlIdArr.length * 2);
                //存放子commonQuery和参数名称
                Map<String, MatchesInfoAndKeyValue> subResultMap = new ConcurrentHashMap<String, MatchesInfoAndKeyValue>();
                MatchesInfoAndKeyValue mv = null;
                for (int i = 0; i < sqlIdArr.length; i++) {
                    CommonQuery suCommonQuery = commonQueryDao.getNoTenantModel(sqlIdArr[i], dataSign);
                    JSONArray ja = JSONArray.fromObject(suCommonQuery.getParamContent());
                    mv = new MatchesInfoAndKeyValue(2 * count);
                    String[] paramsName = new String[ja.size()];
                    for (int j = 0; j < ja.size(); j++) {
                        JSONObject jo = ja.getJSONObject(j);
                        String name = jo.optString("NAME").trim();
                        if (StringUtil.isNullOrBlankTrim(name)) {
                            name = jo.optString("name").trim();
                        }
                        if (StringUtil.isNullOrBlankTrim(name)) {
                            throw new RuntimeException("sqlId为" + sqlIdArr[i] + "的查询中配置name参数值为空");
                        } else {
                            name = name.toUpperCase();
                        }
                        paramsName[j] = name;
                    }
                    mv.setCommonQuery(suCommonQuery);
                    mv.setParamNames(paramsName);
                    subResultMap.put(sqlIdArr[i], mv);
                }
                long catche = System.currentTimeMillis();
                //获取数据
                List<EXCEL_INPUT> inputs = excelInputDao.getEntityListByBatchNo(tenantNumId, dataSign, request.getBatchNo(), 0L);
//							获取缓存值，存入本地map
                for (int j = 0; j < sqlIdArr.length; j++) {
                    String cacheKey = sqlIdArr[j];
                    JSONObject params = null;
                    Set<Object> cacheKeys = new HashSet<Object>();
                    StringBuffer buffer = null;
                    MatchesInfoAndKeyValue keyValue = null;
                    String[] paramsName = null;
                    for (EXCEL_INPUT input : inputs) {
                        params = JSONObject.fromObject(input);
                        keyValue = subResultMap.get(sqlIdArr[j]);
                        paramsName = keyValue.getParamNames();
                        buffer = new StringBuffer();
//									buffer.append(cacheKey);
                        for (int i = 0; i < paramsName.length; i++) {
                            buffer.append("_").append(params.getString(paramsName[i]));
                        }
                        cacheKeys.add(buffer.toString());
                    }
                    List<Object> cacheList = stringRedisTemplate.opsForHash().multiGet(cacheKey, cacheKeys);
                    Object[] keys = cacheKeys.toArray();
                    int cacheSize = cacheList.size();
                    for (int i = 0; i < cacheSize; i++) {
                        if (!"null".equals(cacheList.get(i)) && cacheList.get(i) != null) {
                            tempCacheMap.put(keys[i].toString(), (String) cacheList.get(i));
                        }
                    }
                }
                long time1 = System.currentTimeMillis();
                log.info("cache:" + (time1 - catche));
                List<ExcelInputImportArgs> argList = inputs.parallelStream().map(input -> {
                    ExcelInputImportArgs args = new ExcelInputImportArgs();
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.putAll(JSONObject.fromObject(input));
                        jsonObject.put("tenant_num_id", request.getTenantNumId());
                        jsonObject.put("data_sign", request.getDataSign());
                        if (comquery.getSubSqlId() != null && !comquery.getSubSqlId().isEmpty()) {
                            Map<String, Object> paramsMap = null;
                            for (int j = 0; j < sqlIdArr.length; j++) {
                                StringBuffer buffer = new StringBuffer();
                                MatchesInfoAndKeyValue keyValue = subResultMap.get(sqlIdArr[j]);
                                String[] paramsNames = keyValue.getParamNames();
                                paramsMap = new HashMap<String, Object>();
                                for (int k = 0; k < paramsNames.length; k++) {
                                    buffer.append("_").append(jsonObject.getString(paramsNames[k]));
                                    paramsMap.put(paramsNames[k], jsonObject.getString(paramsNames[k]));
                                }
                                if (tempCacheMap.get(buffer.toString()) == null || tempCacheMap.get(buffer.toString()).equals("null")) {
                                    ExcuteSqlResultModel queryResultModel = commonJsonQueryService.excuteSqlById(0L, keyValue.getCommonQuery().getJdbcName(), keyValue.getCommonQuery(), jsonObject, tenantNumId, dataSign);
                                    if (queryResultModel == null || queryResultModel.getData() == null || queryResultModel.getData().isEmpty()) {
                                        throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40160, keyValue.getCommonQuery().getSqlName() + ";参数:" + paramsMap);
                                    }
                                    jsonObject.putAll(queryResultModel.getData().get(0));
                                    tempCacheMap.put(buffer.toString(), queryResultModel.getData().toString());
                                } else {
                                    List list = gson.fromJson(tempCacheMap.get(buffer.toString()), List.class);
                                    jsonObject.putAll((Map) list.get(0));
                                }
                            }
                        }
                        if (dynamicParams != null) {
                            jsonObject.putAll(dynamicParams);
                        }
                        ExcuteSqlResultModel resModel = commonJsonQueryService.parseSqlById(0L, comquery.getJdbcName(), comquery, jsonObject, tenantNumId, dataSign);
                        args.setArg(resModel.getArg());
                        args.setInputSeries(input.getSERIES());
                    } catch (Exception e) {
                        e.printStackTrace();
                        args.setFailMsg("第" + input.getEXCEL_LINE() + "行" + e.getMessage());
                    }
                    return args;
                }).collect(Collectors.toList());

                int listSize = argList.size();
                long time12 = System.currentTimeMillis() - time1;
                log.info("listSize" + listSize + "解析参数时间:" + time12);
                List<Long> inputSeriesList = new ArrayList<Long>();
                List<Object[]> args = new ArrayList<Object[]>();
                ExcelInputImportArgs excelAgs = null;
                StringBuffer errMsgBuffer = new StringBuffer();
                for (int j = 0; j < listSize; j++) {
                    excelAgs = argList.get(j);
                    inputSeriesList.add(excelAgs.getInputSeries());
                    if (StringUtil.isAllNotNullOrBlank(excelAgs.getFailMsg())) {
                        errMsgBuffer.append(excelAgs.getFailMsg()).append(";");
                    }
                    args.add(excelAgs.getArg());
                }
                String errMsg = errMsgBuffer.toString();
                if (StringUtil.isAllNotNullOrBlank(errMsgBuffer)) {
                    throw new BusinessException(ExportUtil.SUB_SYSTEM, ExceptionType.BE40160, errMsg);
                }
                int insertSize = 2000;
                int insertCount = args.size() % insertSize == 0 ? args.size() / insertSize : (args.size() / insertSize + 1);
                DataSourceContextHolder.clearDataSourceType();
                DataSourceContextHolder.setDataSourceType(comquery.getJdbcName());
                status = dynamicTransactionManager.getTransaction(TransactionUtil.newTransactionDefinition());
                for (int i = 0; i < insertCount; i++) {
                    List<BatchExcuteModel> excuteModelList = new ArrayList<BatchExcuteModel>();
                    BatchExcuteModel batchModel = new BatchExcuteModel();
                    batchModel.setHasNoDataUpdate(false);
                    batchModel.setSql(comquery.getSqlContent());
                    if (i != insertCount - 1) {
                        batchModel.setArgList(args.subList(i * insertSize, (i + 1) * insertSize));
                    } else {
                        batchModel.setArgList(args.subList(i * insertSize, args.size()));
                    }
                    excuteModelList.add(batchModel);
                    commonJsonQueryService.batchExcuteSql(excuteModelList, comquery.getJdbcName());
                }
                dynamicTransactionManager.commit(status);
                excelInputDao.batchUpdateStatusByBatchNo(tenantNumId, dataSign, request.getBatchNo(), 1);
            } catch (Exception e) {
                if (status != null) {
                    dynamicTransactionManager.rollback(status);
                }
                ExceptionUtil.processException(e, response);
            }

        } catch (Exception e) {
            ExceptionUtil.processException(e, response);
        }
        long cost = System.currentTimeMillis() - start;
        log.info("cost:" + cost);
        if (log.isDebugEnabled()) {
            log.debug("end importExcelData return:{}", response.toLowerCaseJson());
        }
        return response;
    }
}
