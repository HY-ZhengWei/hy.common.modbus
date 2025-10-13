package org.hy.common.modbus.junit;

import java.util.List;

import org.hy.common.Help;
import org.hy.common.hart.enums.DataBit;
import org.hy.common.hart.enums.Parity;
import org.hy.common.hart.enums.StopBit;
import org.hy.common.hart.enums.TimeoutMode;
import org.hy.common.hart.serialPort.SerialPortConfig;
import org.hy.common.hart.serialPort.SerialPortFactory;
import org.hy.common.modbus.IModbus;
import org.hy.common.modbus.data.MConnConfig;
import org.hy.common.modbus.enums.ModbusProtocol;
import org.hy.common.modbus.enums.ModbusType;
import org.hy.common.modbus.modbus4j.Modbus4J;
import org.junit.Test;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;





/**
 * 测试单元：热电偶
 * 
 * 注：热电偶的硬件上有要求，一次必须读写2个数据，只读一个数据时报错。
 *     费了九牛二虎之力才发现问题的本原的。
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-09-26
 * @version     v1.0
 */
public class JU_ModbusRTU_Thermocouple
{
    
    @Test
    public void test_HKPyrometer() throws InterruptedException
    {
        List<SerialPortConfig> v_CommPorts = SerialPortFactory.getCommPorts();
        Help.toSort(v_CommPorts ,"commPortName");
        Help.print(v_CommPorts);
        
        MConnConfig v_Config = new MConnConfig();
        v_Config.setProtocol(ModbusProtocol.RTU);
        v_Config.setCommPortName("USB Serial Port (COM5)");
        v_Config.setType(    ModbusType.Master);
        v_Config.setBaudRate(9600);
        v_Config.setParityCheck(Parity.Even);
        v_Config.setDataBits(DataBit.DataBit_8);
        v_Config.setStopBit(StopBit.One);
        v_Config.setTimeout(3000);
        v_Config.setReadTimeout(3000);
        v_Config.setWriteTimeout(3000);   
        v_Config.setTimeoutModes(TimeoutMode.NonBlocking.getValue());
        v_Config.setReconnect(-1);
        
        
        IModbus v_Modbus = new Modbus4J(v_Config);
        if ( v_Modbus.init() )
        {
            for (int i=1; i<=3; i++)
            {
                Number [] v_Value = v_Modbus.readInputRegister(1 ,560 ,DataType.TWO_BYTE_INT_UNSIGNED ,2);
                System.out.println("读取到的数: " + toFloat(v_Value[0].intValue() ,v_Value[1].intValue()));
                Help.print(v_Value);
                
                v_Value = v_Modbus.readInputRegister(1 ,562 ,DataType.TWO_BYTE_INT_UNSIGNED ,2);
                Help.print(v_Value);
                Thread.sleep(1000);
            }
            
            v_Modbus.close();
        }
        else
        {
            System.out.println("初始化Modbus异常");
        }
    }
    
    
    
    /**
     * 高低位两整数转为浮点数。算法：高低位合并 + 缩放还原
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-09-17
     * @version     v1.0
     *
     * @param i_High  高位数值
     * @param i_Low   低位数值
     * @return
     */
    public static Float toFloat(int i_High ,int i_Low)
    {
        return (((long) i_High << 16) | i_Low) / 1F;
    }
    
    
    
    // java -Dlog4j.configurationFile=./classes/config/ms.log4j2.xml -cp "classes:lib/*:/opt/tomcat/lib/*" org.hy.common.modbus.junit.JU_ModbusRTU_Thermocouple
    public static void main(String [] i_Args) throws InterruptedException
    {
        List<String> v_CommPortNames = SerialPortFactory.getCommPortNames();
        Help.toSort(v_CommPortNames);
        Help.print(v_CommPortNames);
        
        MConnConfig v_Config = new MConnConfig();
        v_Config.setProtocol(ModbusProtocol.RTU);
        v_Config.setCommPortName("/dev/ttyS3");
        v_Config.setType(    ModbusType.Master);
        v_Config.setBaudRate(9600);
        v_Config.setParityCheck(Parity.Even);
        v_Config.setDataBits(DataBit.DataBit_8);
        v_Config.setStopBit(StopBit.One);
        v_Config.setTimeout(3000);
        v_Config.setReadTimeout(3000);
        v_Config.setWriteTimeout(3000);   
        v_Config.setTimeoutModes(TimeoutMode.NonBlocking.getValue());
        v_Config.setReconnect(-1);
        
        /*
        test("功能040 基于0" ,v_Config ,0 ,4);  System.out.println("===============================");
        test("功能040 基于1" ,v_Config ,1 ,4);  System.out.println("===============================");
        test("功能041 基于0" ,v_Config ,0 ,41); System.out.println("===============================");
        test("功能041 基于1" ,v_Config ,1 ,41); System.out.println("===============================");
        
        test2("功能040 基于0" ,v_Config ,0);  System.out.println("===============================");
        test2("功能040 基于1" ,v_Config ,1);  System.out.println("===============================");
        */
        
        test("功能030 基于0" ,v_Config ,0 ,3);  System.out.println("===============================");
        
        /*
        test("功能030 基于1" ,v_Config ,1 ,3);  System.out.println("===============================");
        test("功能031 基于0" ,v_Config ,0 ,31); System.out.println("===============================");
        test("功能031 基于1" ,v_Config ,1 ,31); System.out.println("===============================");
        */
        
        
        IModbus v_Modbus = new Modbus4J(v_Config);
        if ( v_Modbus.init() )
        {
            for (int i=1; i<=10; i++)
            {
                Number [] v_Value = v_Modbus.readOutputRegister(1 ,0 ,DataType.TWO_BYTE_INT_SIGNED ,2);
                System.out.println("读取到的数: ");
                Help.print(v_Value);
                Thread.sleep(1000);
            }
        }
        else
        {
            System.out.println("初始化Modbus异常");
        }
    }
    
    
    
    public static void test(String i_Comment ,MConnConfig i_Config ,int startOffset ,int i_Type)
    {
        try
        {
            IModbus v_Modbus = new Modbus4J(i_Config);
            if ( v_Modbus.init() )
            {
                System.out.println(i_Comment + "初始化成功");
                
                int[] v_Ints = readRegisters(((Modbus4J)v_Modbus).getMaster() ,1 ,startOffset ,2 ,i_Type);
                System.out.println(i_Comment + "读取到的数: ");
                Help.print(v_Ints);
            }
            else
            {
                System.out.println(i_Comment + "初始化Modbus异常");
            }
            v_Modbus.close();
        }
        catch (Exception exce)
        {
            exce.printStackTrace();
        }
    }
    
    
    
    /**
     * 读取多个输入寄存器的辅助方法（功能码 04）
     * 注意：Modbus4j 的 API 设计更倾向于使用 Locator，但读取连续原始寄存器需另寻他法。
     * 以下是一种可能的实现思路（可能需要根据实际API调整）：
     * @throws ErrorResponseException 
     */
    private static int[] readRegisters(ModbusMaster master, int slaveId, int startAddress, int length ,int type) throws ModbusTransportException, ErrorResponseException {
        
        // 这里需要实现读取多个输入寄存器的逻辑。
        // 由于 modbus4j 的 API 设计，你可能需要使用 BasicModbusClient 或直接发送请求。
        // 一种常见方式的示例（伪代码），实际使用请参考 modbus4j 文档或源码：
        BatchRead<Number> batch = new BatchRead<Number>();
        for (int i = 0; i < length; i++) {
            // 创建用于读取输入寄存器的定位器（功能码 04）
            if ( type == 4 )
            {
                batch.addLocator(i, BaseLocator.inputRegister     (slaveId, startAddress + i, DataType.TWO_BYTE_INT_SIGNED));
            }
            else if ( type == 41 )
            {
                batch.addLocator(i, BaseLocator.inputRegisterBit  (slaveId, startAddress + i, DataType.TWO_BYTE_INT_SIGNED));
            }
            else if ( type == 3 )
            {
                batch.addLocator(i, BaseLocator.holdingRegister   (slaveId, startAddress + i, DataType.TWO_BYTE_INT_SIGNED));
            }
            else if ( type == 31 )
            {
                batch.addLocator(i, BaseLocator.holdingRegisterBit(slaveId, startAddress + i, DataType.TWO_BYTE_INT_SIGNED));
            }
        }
        batch.setContiguousRequests(true); // 设置为连续请求，优化通信
        BatchResults<Number> results = master.send(batch);

        int[] values = new int[length];
        if ( results != null )
        {
            for (int i = 0; i < length; i++) {
                Object v_Value = results.getValue(i);
                if ( v_Value == null )
                {
                    values[i] = -99999;
                }
                else
                {
                    values[i] = (short) results.getValue(i);
                }
            }
        }
        return values;
    }
    
    
    
    public static void test2(String i_Comment ,MConnConfig i_Config ,int startOffset)
    {
        try
        {
            IModbus v_Modbus = new Modbus4J(i_Config);
            if ( v_Modbus.init() )
            {
                System.out.println(i_Comment + "初始化成功");
                Thread.sleep(1000);
                
                int[] v_Ints = readInputRegisters(((Modbus4J)v_Modbus).getMaster() ,1 ,startOffset ,1);
                System.out.println(i_Comment + "读取到的数: " + v_Ints);
            }
            else
            {
                System.out.println(i_Comment + "初始化Modbus异常");
            }
        }
        catch (Exception exce)
        {
            exce.printStackTrace();
        }
    }
    
    
    private static int[] readInputRegisters(ModbusMaster master, int slaveId, int startAddress, int length) 
            throws ModbusTransportException, ErrorResponseException {
            
            master.setRetries(3);
        
            // 直接创建读取输入寄存器请求（功能码04）
            ReadInputRegistersRequest request = new ReadInputRegistersRequest(slaveId, startAddress, length);
            ReadInputRegistersResponse response = (ReadInputRegistersResponse) master.send(request);
            
            if (response.isException()) {
                throw new ModbusTransportException("Modbus异常响应: " + response.getExceptionMessage());
            }
            
            short[] data = response.getShortData();
            int[] values = new int[data.length];
            for (int i = 0; i < data.length; i++) {
                values[i] = data[i] & 0xFFFF; // 转换为无符号整数
            }
            
            return values;
        }
}
