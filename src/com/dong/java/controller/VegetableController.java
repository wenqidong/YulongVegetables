package com.dong.java.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.dong.java.entity.ConsigneeEntity;
import com.dong.java.entity.GoodEntity;
import com.dong.java.entity.PriceEntity;
import com.dong.java.persistence.PersistenceTool;
import com.dong.java.util.NumberToCN;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VegetableController {

    @FXML
    private Button btAddVegetable;
    @FXML
    private Button btExportVegetable;
    @FXML
    private Button btAddPropVegetable;
    @FXML
    private Button btAddPropConsignee;

    @FXML
    private TextField txtInputVegetable;
    @FXML
    private TextField txtInputAmount;
    @FXML
    private TextField txtInputAddress;
    @FXML
    private TextField txtInputPhone;

    @FXML
    private TextField txtInputVegetableAdd;
    @FXML
    private TextField txtInputPriceAdd;
    @FXML
    private TextField txtInputNameAdd;
    @FXML
    private TextField txtInputAddressAdd;
    @FXML
    private TextField txtInputPhoneAdd;

    @FXML
    private TableView tableVegetable;
    @FXML
    private TableColumn<GoodEntity, String> vegetableColumnName;
    @FXML
    private TableColumn<GoodEntity, String> vegetableColumnUnit;
    @FXML
    private TableColumn<GoodEntity, Integer> vegetableColumnAmount;
    @FXML
    private TableColumn<GoodEntity, Float> vegetableColumnPrice;
    @FXML
    private TableColumn<GoodEntity, Float> vegetableColumnTotal;
    @FXML
    private TableColumn<GoodEntity, String> vegetableColumnMemo;
    @FXML
    private ChoiceBox<String> cBoxConsignee;
    @FXML
    private TableView tablePrice;
    @FXML
    private TableColumn<Map, String> priceColumnName;
    @FXML
    private TableColumn<Map, String> priceColumnPrice;
    @FXML
    private TableView tableConsignee;
    @FXML
    private TableColumn<Map, String> consigneeColumnName;
    @FXML
    private TableColumn<Map, String> consigneeColumnAddress;
    @FXML
    private TableColumn<Map, String> consigneeColumnPhone;

    private ConsigneeEntity selectOne;

    private int tablePriceCurr = 0;

    @FXML
    public void onVegetableAdd(ActionEvent event) {
        String vegetable = txtInputVegetable.getText();
        String amount = txtInputAmount.getText();

        if (vegetable != null && amount != null) {
            String price = "";
            PersistenceTool p = PersistenceTool.getInstance();
            List<PriceEntity> vList = p.getVegetableInfo();
            for (PriceEntity priceEntity : vList) {
                if (priceEntity.getName().equals(vegetable)) {
                    price = priceEntity.getPrice();
                }
            }

            if (price.equals("")) {
                return;
            }
            GoodEntity good = new GoodEntity();
            good.setName(vegetable);
            good.setUnit("斤");
            good.setAmount(1);
            good.setPrice(Float.valueOf(price));
            java.text.DecimalFormat  df  =new java.text.DecimalFormat("#.0");
            String total = df.format(Float.valueOf(amount) * Float.valueOf(price));
            good.setTotal(Float.valueOf(total));
            good.setMemo("无");
            good.setNumber(goodData.size() + 1);
            goodData.add(good);
            txtInputVegetable.clear();
        }
    }

    @FXML
    public void onVegetableExport(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出清单");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLS Files", "*.xls"));
        File file = fileChooser.showSaveDialog(new Stage());
        if(file != null) {
            System.out.println("选择文件...");
            generateExcel(file);
        }
    }

    @FXML
    public void onPropVegetableAdd(ActionEvent event) {
        String name = txtInputVegetableAdd.getText();
        String price = txtInputPriceAdd.getText();
        if (!name.isEmpty() && !price.isEmpty()) {
            PriceEntity priceEntity = new PriceEntity(name, price);
            priceData.add(priceEntity);
            PersistenceTool p = PersistenceTool.getInstance();
            Map m = new HashMap();
            m.put("name", name);
            m.put("price", price);
            p.addProperties("vegetables", m);
        }
    }

    @FXML
    public void onPropAddConsignee(ActionEvent event) {
        String name = txtInputNameAdd.getText();
        String address = txtInputAddressAdd.getText();
        String phone = txtInputPhoneAdd.getText();
        if (!name.isEmpty() && !address.isEmpty() && !phone.isEmpty()) {
            ConsigneeEntity consigneeEntity = new ConsigneeEntity(name, address, phone);
            consigneeInfoData.add(consigneeEntity);
            PersistenceTool p = PersistenceTool.getInstance();
            Map m = new HashMap();
            m.put("name", name);
            m.put("address", address);
            m.put("phone", phone);
            p.addProperties("consignees", m);

            List<String> consigneeList = p.getConsigneeInfo().stream().map(ConsigneeEntity::getName).collect(Collectors.toList());
            consigneeData.clear();
            consigneeData.addAll(consigneeList);
        }
    }

    @FXML
    public void onResetTableView(ActionEvent event) {
        goodData.clear();
    }

    /**
     * 删除蔬菜信息
     */
    @FXML
    public void onDelVegetableInfo(ActionEvent event) {
        PriceEntity priceEntity = (PriceEntity) tablePrice.getSelectionModel().getSelectedItem();
        priceData.remove(priceEntity);

        PersistenceTool p = PersistenceTool.getInstance();
        p.removeProperties("vegetables", priceEntity.getName());
    }

    /**
     * 删除收货人信息
     */
    @FXML
    public void onDelConsigneeInfo(ActionEvent event) {
        ConsigneeEntity consigneeEntity = (ConsigneeEntity) tableConsignee.getSelectionModel().getSelectedItem();
        consigneeInfoData.remove(consigneeEntity);

        PersistenceTool p = PersistenceTool.getInstance();
        p.removeProperties("consignees", consigneeEntity.getName());
    }

    private void generateExcel(File file) {
        String templateFileName = "D:\\yulong\\template.xlsx";

        String fileName = file.getPath();
        ExcelWriter excelWriter = EasyExcel.write(fileName).excelType(ExcelTypeEnum.XLSX).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        // 直接写入数据
        excelWriter.fill(goodData, fillConfig, writeSheet);
//        excelWriter.fill(goodData, fillConfig, writeSheet);

        // 写入list之前的数据
        Map<String, Object> map = new HashMap<>();
        map.put("consignee", selectOne.getName());
        map.put("year", "2019");
        map.put("month", "10");
        map.put("day", "9");
        map.put("address", selectOne.getAddress());
        map.put("phone", selectOne.getPhone());
        map.put("moneyStr", getTotalMoney(goodData));
        excelWriter.fill(map, writeSheet);
        excelWriter.finish();
        System.out.println("生成完毕...");
    }

    private String getTotalMoney(ObservableList<GoodEntity> goodData) {
        StringBuilder sb = new StringBuilder();
        float total = 0f;
        for (GoodEntity goodDatum : goodData) {
            int totalNum = goodDatum.getAmount();
            total += totalNum * goodDatum.getTotal();
        }

        sb.append(NumberToCN.number2CNMontrayUnit(BigDecimal.valueOf(total))).append("  ￥").append(String.format("%.1f", total));
        return sb.toString();
    }

    @FXML
    private void initialize() {
        vegetableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        vegetableColumnUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        vegetableColumnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        vegetableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        vegetableColumnTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        vegetableColumnMemo.setCellValueFactory(new PropertyValueFactory<>("memo"));
        tableVegetable.setItems(goodData);
        tableVegetable.setEditable(true);

        PersistenceTool p = PersistenceTool.getInstance();
        List<String> consigneeList = p.getConsigneeInfo().stream().map(ConsigneeEntity::getName).collect(Collectors.toList());
        consigneeData.addAll(consigneeList);
        cBoxConsignee.setItems(consigneeData);

        priceColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tablePrice.setItems(priceData);
        tablePrice.setEditable(true);
        List<PriceEntity> vegetableInfo = p.getVegetableInfo();
        priceData.addAll(vegetableInfo);

        consigneeColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        consigneeColumnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        consigneeColumnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tableConsignee.setItems(consigneeInfoData);
        tableConsignee.setEditable(true);
        List<ConsigneeEntity> consigneeInfo = p.getConsigneeInfo();
        consigneeInfoData.addAll(consigneeInfo);

        cBoxConsignee.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            selectOne = consigneeInfo.get(newValue.intValue());
            txtInputAddress.setText(selectOne.getAddress());
            txtInputPhone.setText(selectOne.getPhone());
        });

    }

    private final ObservableList<GoodEntity> goodData = FXCollections.observableArrayList();

    private final ObservableList<String> consigneeData = FXCollections.observableArrayList();

    private final ObservableList<PriceEntity> priceData = FXCollections.observableArrayList();

    private final ObservableList<ConsigneeEntity> consigneeInfoData = FXCollections.observableArrayList();
}
