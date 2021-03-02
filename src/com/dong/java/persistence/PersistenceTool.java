package com.dong.java.persistence;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dong.java.entity.ConsigneeEntity;
import com.dong.java.entity.PriceEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 持久化收货人信息以及蔬菜价格
 *
 * @author wenqi
 * @date 2021/2/28 20:18
 */
public class PersistenceTool {

    private static PersistenceTool persistenceTool = null;

    public static PersistenceTool getInstance() {
        if (null == persistenceTool) {
            persistenceTool = new PersistenceTool();
        }

        return persistenceTool;
    }

    /**
     * 保存蔬菜信息，蔬菜名、单价
     */
    private List<PriceEntity> vegetableInfo = new ArrayList<>();

    /**
     * 保存收货人信息
     */
    private List<ConsigneeEntity> consigneeInfo = new ArrayList<>();

    /**
     * 内存中的全局配置
     */
    private String globProp;

    /**
     * 配置文件位置
     */
    private final static String path = "D:\\yulong\\yulong.json";

    /**
     * 更新内存中的配置
     */
    private void updateProperties() {
        if (globProp.length() != 0) {
            JSONObject info = JSONObject.parseObject(globProp);
            JSONArray vegetableInfoArray = info.getJSONArray("vegetables");
            vegetableInfo.clear();
            vegetableInfoArray.forEach(obj -> {
                Map v = (Map) obj;
                vegetableInfo.add(new PriceEntity((String) v.get("name"), (String) v.get("price")));
            });
            JSONArray consigneeInfoArray = info.getJSONArray("consignees");
            consigneeInfo.clear();
            consigneeInfoArray.forEach(cIA -> {
                Map c = (Map) cIA;
                consigneeInfo.add(new ConsigneeEntity((String) c.get("name"), (String) c.get("address"), (String) c.get("phone")));
            });
        }
    }

    /**
     * 新增配置信息
     *
     * @param type    配置类型：蔬菜/地址
     * @param newInfo 配置信息
     */
    public void addProperties(String type, Map newInfo) {
        if (globProp.length() != 0) {
            JSONObject info = JSONObject.parseObject(globProp);
            if ("vegetables".equals(type)) {
                JSONArray vegetableInfoArray = info.getJSONArray("vegetables");
                Iterator<Object> vIterator1 = vegetableInfoArray.iterator();
                while (vIterator1.hasNext()) {
                    Map v = (Map) vIterator1.next();
                    if (v.get("name").equals(newInfo.get("name"))) {
                        vIterator1.remove();
                    }
                }
                vegetableInfoArray.add(newInfo);
            }

            if ("consignees".equals(type)) {
                JSONArray consigneeInfoArray = info.getJSONArray("consignees");
                Iterator<Object> vIterator2 = consigneeInfoArray.iterator();
                while (vIterator2.hasNext()) {
                    Map v = (Map) vIterator2.next();
                    if (v.get("name").equals(newInfo.get("name"))) {
                        vIterator2.remove();
                    }
                }
                consigneeInfoArray.add(newInfo);
            }

            globProp = info.toJSONString();
            updateProperties();
            saveDataToFile();
        }
    }

    /**
     * 移除配置
     *
     * @param type 配置类型：蔬菜/地址
     * @param name 配置名称
     */
    public void removeProperties(String type, String name) {
        if (globProp.length() != 0) {
            JSONObject info = JSONObject.parseObject(globProp);
            if ("vegetables".equals(type)) {
                JSONArray vegetableInfoArray = info.getJSONArray("vegetables");
                Iterator<Object> vIterator1 = vegetableInfoArray.iterator();
                while (vIterator1.hasNext()) {
                    Map v = (Map) vIterator1.next();
                    if (v.get("name").equals(name)) {
                        vIterator1.remove();
                    }
                }
            }

            if ("consignees".equals(type)) {
                JSONArray consigneeInfoArray = info.getJSONArray("consignees");
                Iterator<Object> vIterator2 = consigneeInfoArray.iterator();
                while (vIterator2.hasNext()) {
                    Map v = (Map) vIterator2.next();
                    if (v.get("name").equals(name)) {
                        vIterator2.remove();
                    }
                }
            }

            globProp = info.toJSONString();
            updateProperties();
            saveDataToFile();
        }
    }

    private void saveDataToFile() {
        BufferedWriter writer = null;
        File file = new File(path);
        //如果文件不存在，则新建一个
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写入
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
            writer.write(globProp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("文件写入成功！");
    }

    public void getDatafromFile() {
        BufferedReader reader = null;
        StringBuilder lastStr = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                lastStr.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        globProp = lastStr.toString();
        updateProperties();
    }

    public void setGlobProp(String prop) {
        globProp = prop;
    }

    public List<PriceEntity> getVegetableInfo() {
        return vegetableInfo;
    }

    public List<ConsigneeEntity> getConsigneeInfo() {
        return consigneeInfo;
    }
}
