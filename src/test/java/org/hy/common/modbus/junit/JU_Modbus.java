package org.hy.common.modbus.junit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.hart.enums.DataBit;
import org.hy.common.hart.enums.Parity;
import org.hy.common.hart.enums.StopBit;
import org.hy.common.hart.serialPort.SerialPortFactory;
import org.hy.common.modbus.IModbus;
import org.hy.common.modbus.data.MConnConfig;
import org.hy.common.modbus.data.MDataItem;
import org.hy.common.modbus.enums.ModbusData;
import org.hy.common.modbus.enums.ModbusProtocol;
import org.hy.common.modbus.enums.ModbusType;
import org.hy.common.modbus.junit.cflow025.program.LEDData;
import org.hy.common.modbus.modbus4j.Modbus4J;
import org.junit.Test;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;





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
    
    
    
    @Test
    public void test_getCommPortNames()
    {
        List<String> v_DataList = SerialPortFactory.getCommPortNames();
        Help.print(v_DataList);
    }
    
    
    
    @Test
    public void test_Modbus4j_RTU_高温计() throws ModbusTransportException, ErrorResponseException
    {
        MConnConfig v_Config = new MConnConfig();
        v_Config.setProtocol(ModbusProtocol.RTU);
        v_Config.setCommPortName("USB Serial Port (COM5)");
        v_Config.setType(    ModbusType.Master);
        v_Config.setBaudRate(9600);
        v_Config.setParityCheck(Parity.Even);
        v_Config.setDataBits(DataBit.DataBit_8);
        v_Config.setStopBit(StopBit.One);
        
        IModbus v_Modbus = new Modbus4J(v_Config);
        v_Modbus.init();
        
        
        int slaveId = 1; // 从站地址
        int startRegister = 560; // 起始寄存器地址（输入寄存器）
        int numberOfRegisters = 2; // 需要读取2个寄存器（32位）
        int[] registerValues = readInputRegisters(((Modbus4J) v_Modbus).getMaster(), slaveId, startRegister, numberOfRegisters);

        // 4. 获取两个寄存器的值
        int reg1 = registerValues[0]; // 第一个寄存器值（可能是高位或低位，取决于字节序）
        int reg2 = registerValues[1]; // 第二个寄存器值

        // 5. 合并寄存器并转换为 Float (假设设备字节序为高位字在前)
        float result = convertRegistersToFloatBigEndian(reg1, reg2);
        System.out.println("读取到的浮点数: " + result);
    }
    
    
    
    /**
     * 读取多个输入寄存器的辅助方法（功能码 04）
     * 注意：Modbus4j 的 API 设计更倾向于使用 Locator，但读取连续原始寄存器需另寻他法。
     * 以下是一种可能的实现思路（可能需要根据实际API调整）：
     * @throws ErrorResponseException 
     */
    private static int[] readInputRegisters(ModbusMaster master, int slaveId, int startAddress, int length) throws ModbusTransportException, ErrorResponseException {
        // 这里需要实现读取多个输入寄存器的逻辑。
        // 由于 modbus4j 的 API 设计，你可能需要使用 BasicModbusClient 或直接发送请求。
        // 一种常见方式的示例（伪代码），实际使用请参考 modbus4j 文档或源码：
        com.serotonin.modbus4j.BatchRead<Integer> batch = new com.serotonin.modbus4j.BatchRead<Integer>();
        for (int i = 0; i < length; i++) {
            // 创建用于读取输入寄存器的定位器（功能码 04）
            batch.addLocator(i, BaseLocator.inputRegister(slaveId, startAddress + i, DataType.TWO_BYTE_INT_UNSIGNED));
        }
        batch.setContiguousRequests(true); // 设置为连续请求，优化通信
        com.serotonin.modbus4j.BatchResults<Integer> results = master.send(batch);

        int[] values = new int[length];
        for (int i = 0; i < length; i++) {
            values[i] = (int) results.getValue(i);
        }
        return values;
    }

    /**
     * 将两个寄存器值转换为 Float
     * @param firstRegister  第一个寄存器的值
     * @param secondRegister 第二个寄存器的值
     * @param isHighWordFirst  true 表示设备字节序为高位字在前（大端字序），false 表示低位字在前（小端字序）
     * @return 转换后的 float 值
     */
    public static float convertRegistersToFloat(int firstRegister, int secondRegister, boolean isHighWordFirst) {
        int combinedInt;
        if (isHighWordFirst) {
            // 高位字在前：第一个寄存器是浮点数的高16位，第二个是低16位
            combinedInt = (firstRegister << 16) | secondRegister;
        } else {
            // 低位字在前：第一个寄存器是浮点数的低16位，第二个是高16位
            combinedInt = (secondRegister << 16) | firstRegister;
        }
        return Float.intBitsToFloat(combinedInt);
    }
    
    
    /**
     * 针对大端字节序和大端字序设备的转换方法
     * @param firstRegister 第一个寄存器的值（高16位）
     * @param secondRegister 第二个寄存器的值（低16位）
     * @return 转换后的 float 值
     */
    public static float convertRegistersToFloatBigEndian(int firstRegister, int secondRegister) {
        // 对于大端字序：第一个寄存器就是高16位，第二个寄存器是低16位，直接合并
        int combinedInt = (firstRegister << 16) | secondRegister;
        return Float.intBitsToFloat(combinedInt);
    }
}
