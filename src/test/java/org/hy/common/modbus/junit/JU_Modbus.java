package org.hy.common.modbus.junit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.modbus.IModbus;
import org.hy.common.modbus.data.MConnConfig;
import org.hy.common.modbus.data.MDataItem;
import org.hy.common.modbus.enums.ModbusData;
import org.hy.common.modbus.enums.ModbusProtocol;
import org.hy.common.modbus.enums.ModbusType;
import org.hy.common.modbus.modbus4j.Modbus4J;
import org.junit.Test;





/**
 * 测试单元
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-01-20
 * @version     v1.0
 */
public class JU_Modbus
{
    
    @Test
    public void test_Modbus4j_TCP()
    {
        MConnConfig v_Config = new MConnConfig();
        v_Config.setHost("10.1.117.240");
        v_Config.setPort(502);
        v_Config.setProtocol(ModbusProtocol.TCP);
        v_Config.setType(    ModbusType.Master);
        
        IModbus v_Modbus = new Modbus4J(v_Config);
        v_Modbus.init();
        
        for (int index=0; index<10; index++)
        {
            System.out.println("读取输出线圈（功能码0x01）" + v_Modbus.readOutputStatus(1 ,index));
        }
        
        for (int index=0; index<10; index++)
        {
            System.out.println("读取输出寄存器（功能码0x03）" + v_Modbus.readOutputRegister(1 ,index ,ModbusData.D2B_Int_Signed.getValue()));
        }
    }
    
    
    
    @Test
    public void test_Modbus4j_TCP_热处理炉()
    {
        MConnConfig v_Config = new MConnConfig();
        v_Config.setHost("10.1.173.241");
        v_Config.setPort(502);
        v_Config.setProtocol(ModbusProtocol.TCP);
        v_Config.setType(    ModbusType.Master);
        
        IModbus v_Modbus = new Modbus4J(v_Config);
        v_Modbus.init();
        
        Date v_STime = new Date();
        System.out.println("年" + v_Modbus.readOutputRegister(1 ,0 ,ModbusData.D2B_Int_Signed.getValue()));
        System.out.println("月" + v_Modbus.readOutputRegister(1 ,1 ,ModbusData.D2B_Int_Signed.getValue()));
        System.out.println("日" + v_Modbus.readOutputRegister(1 ,2 ,ModbusData.D2B_Int_Signed.getValue()));
        System.out.println("时" + v_Modbus.readOutputRegister(1 ,3 ,ModbusData.D2B_Int_Signed.getValue()));
        System.out.println("分" + v_Modbus.readOutputRegister(1 ,4 ,ModbusData.D2B_Int_Signed.getValue()));
        System.out.println("秒" + v_Modbus.readOutputRegister(1 ,5 ,ModbusData.D2B_Int_Signed.getValue()));
        
        for (int x=1; x<=10; x++)
        {
            System.out.println("CH" + x + "实时值" + v_Modbus.readOutputRegister(1 ,5 + x ,ModbusData.D2B_Int_Signed.getValue()));
        }
        
        Date v_ETime = new Date();
        System.out.println(Date.toTimeLen(v_ETime.differ(v_STime)));
    }
    
    
    
    @Test
    public void test_Modbus4j_TCP_热处理炉_批量读() throws InterruptedException
    {
        MConnConfig v_Config = new MConnConfig();
        v_Config.setHost("10.1.173.241");
        v_Config.setPort(502);
        v_Config.setProtocol(ModbusProtocol.TCP);
        v_Config.setType(    ModbusType.Master);
        
        IModbus v_Modbus = new Modbus4J(v_Config);
        v_Modbus.init();
        
        for (int x=1; x<=60; x++)
        {
            Date      v_STime = new Date();
            Number [] v_Nums  = v_Modbus.readOutputRegister(1 ,0 ,ModbusData.D2B_Int_Signed.getValue() ,16);
            Date      v_ETime = new Date();
            System.out.println(Date.toTimeLen(v_ETime.differ(v_STime)));
            
            Help.print(v_Nums);
            Thread.sleep(1000);
        }
    }
    
    
    
    @Test
    public void test_Modbus4j_TCP_热处理炉_批量读_数据报文() throws InterruptedException
    {
        List<MDataItem> v_Datagram = new ArrayList<MDataItem>();
        v_Datagram.add(new MDataItem("年"     ,0  ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("月"     ,1  ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("日"     ,2  ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("时"     ,3  ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("分"     ,4  ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("秒"     ,5  ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("温度01" ,6  ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("温度02" ,7  ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("温度03" ,8  ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("温度04" ,9  ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("温度05" ,10 ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("温度06" ,11 ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("温度07" ,12 ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("温度08" ,13 ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("温度09" ,14 ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("温度10" ,15 ,ModbusData.D2B_Int_Signed));
        
        MConnConfig v_Config = new MConnConfig();
        v_Config.setHost("10.1.117.243");
        v_Config.setPort(502);
        v_Config.setProtocol(ModbusProtocol.TCP);
        v_Config.setType(    ModbusType.Master);
        
        IModbus v_Modbus = new Modbus4J(v_Config);
        v_Modbus.init();
        
        for (int x=1; x<=60; x++)
        {
            Date                v_STime = new Date();
            Map<String ,Object> v_Nums  = v_Modbus.readOutputRegister(1 ,v_Datagram);
            Date                v_ETime = new Date();
            System.out.println(Date.toTimeLen(v_ETime.differ(v_STime)));
            
            Help.print(v_Nums);
            Thread.sleep(1000);
        }
    }
    
    
    
    @Test
    public void test_Modbus4j_TCP_LED显示屏_报文批量写()
    {
        List<MDataItem> v_Datagram = new ArrayList<MDataItem>();
        v_Datagram.add(new MDataItem("时间1" ,0    ,ModbusData.DVarchar         ," 10:00:00"));
        v_Datagram.add(new MDataItem("温度1" ,8    ,ModbusData.DVarchar         ,"1000C"));
        v_Datagram.add(new MDataItem("时间2" ,16   ,ModbusData.DVarchar         ," 09:00:00"));
        v_Datagram.add(new MDataItem("温度2" ,4291 ,ModbusData.D2B_Int_Signed   ,"1200"));
        v_Datagram.add(new MDataItem("时间3" ,32   ,ModbusData.DVarchar         ," 08:00:00"));
        v_Datagram.add(new MDataItem("温度3" ,4293 ,ModbusData.D2B_Int_Unsigned ,"1500"));
        v_Datagram.add(new MDataItem("时间4" ,48   ,ModbusData.DVarchar         ," 07:00:00"));
        v_Datagram.add(new MDataItem("温度4" ,56   ,ModbusData.DVarchar         ,"1600C"));
        
        MConnConfig v_Config = new MConnConfig();
        v_Config.setHost("192.168.0.101");
        v_Config.setPort(502);
        v_Config.setProtocol(ModbusProtocol.TCP);
        v_Config.setType(    ModbusType.Master);
        
        IModbus v_Modbus = new Modbus4J(v_Config);
        v_Modbus.init();
        
        v_Modbus.writes(1 ,v_Datagram);
    }
    
    
    
    @Test
    public void test_Modbus4j_TCP_LED显示屏() throws InterruptedException
    {
        Thread.sleep(10000);
        
        MConnConfig v_Config = new MConnConfig();
        v_Config.setHost("192.168.0.101");
        v_Config.setPort(502);
        v_Config.setProtocol(ModbusProtocol.TCP);
        v_Config.setType(    ModbusType.Master);
        
        IModbus v_Modbus = new Modbus4J(v_Config);
        v_Modbus.init();
        
        List<LEDData> v_Datas  = new ArrayList<LEDData>();
        int           v_Index  = -1;
        int           v_TimeGS = 5;
        
        for (int x=1000; x<=160000; x++)
        {
            Date v_Time = new Date();
            v_Time = v_Time.getTimeGroupSecond(v_TimeGS);
            
            LEDData v_Data = new LEDData();
            v_Data.setTime(v_Time);
            v_Data.setTemperature(Help.random(0 ,2000));
            
            if ( v_Index >= 0 )
            {
                if ( v_Datas.get(v_Index).getTime().equals(v_Time) )
                {
                    Thread.sleep(1000);
                    continue;
                }
            }
            
            v_Datas.add(v_Data);
            v_Index++;
            
            if ( v_Index - 3 >= 0 )
            {
                System.out.println(v_Modbus.writeString(1 ,48 ," " + v_Datas.get(v_Index - 3).getTime().getHMS()));
                System.out.println(v_Modbus.writeString(1 ,56 ,StringHelp.lpad(v_Datas.get(v_Index - 3).getTemperature() ,4 ," ") + "C"));
            }
            else
            {
                System.out.println(v_Modbus.writeString(1 ,48 ," "));
                System.out.println(v_Modbus.writeString(1 ,56 ," "));
            }
            
            if ( v_Index - 2 >= 0 )
            {
                System.out.println(v_Modbus.writeString(1 ,32 ," " + v_Datas.get(v_Index - 2).getTime().getHMS()));
                System.out.println(v_Modbus.writeString(1 ,40 ,StringHelp.lpad(v_Datas.get(v_Index - 2).getTemperature() ,4 ," ") + "C"));
            }
            else
            {
                System.out.println(v_Modbus.writeString(1 ,32 ," "));
                System.out.println(v_Modbus.writeString(1 ,40 ," "));
            }
            
            if ( v_Index - 1 >= 0 )
            {
                System.out.println(v_Modbus.writeString(1 ,16 ," " + v_Datas.get(v_Index - 1).getTime().getHMS()));
                System.out.println(v_Modbus.writeString(1 ,24 ,StringHelp.lpad(v_Datas.get(v_Index - 1).getTemperature() ,4 ," ") + "C"));
            }
            else
            {
                System.out.println(v_Modbus.writeString(1 ,16 ," "));
                System.out.println(v_Modbus.writeString(1 ,24 ," "));
            }
            
            System.out.println(v_Modbus.writeString(1 ,0  ," " + v_Datas.get(v_Index).getTime().getHMS()));
            System.out.println(v_Modbus.writeString(1 ,8  ,StringHelp.lpad(v_Datas.get(v_Index).getTemperature() ,4 ," ") + "C"));
            
            Thread.sleep(1000);
        }
    }
    
    
    
    
    
    class LEDData
    {
        /** 时间 */
        private Date    time;
        
        /** 温度 */
        private Integer temperature;

        
        
        /**
         * 获取：时间
         */
        public Date getTime()
        {
            return time;
        }

        
        /**
         * 设置：时间
         * 
         * @param i_Time 时间
         */
        public void setTime(Date i_Time)
        {
            this.time = i_Time;
        }

        
        /**
         * 获取：温度
         */
        public Integer getTemperature()
        {
            return temperature;
        }

        
        /**
         * 设置：温度
         * 
         * @param i_Temperature 温度
         */
        public void setTemperature(Integer i_Temperature)
        {
            this.temperature = i_Temperature;
        }
        
    }
    
}
