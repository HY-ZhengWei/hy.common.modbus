package org.hy.common.modbus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hy.common.modbus.data.MDataItem;





/**
 * Modbus统一接口，是各种类库的统一接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-01-20
 * @version     v1.0
 */
public interface IModbus
{
    
    /** 波特率 */
    public static final List<Integer> $BaudRates = Arrays.asList(
                                                     110
                                                    ,300
                                                    ,600
                                                    ,1200
                                                    ,2400
                                                    ,4800
                                                    ,9600
                                                    ,14400
                                                    ,19200
                                                    ,38400
                                                    ,56000
                                                    ,57600
                                                    ,115200
                                                    ,128000
                                                    ,256000);

    
    
    /**
     * 关闭连接，释放资源
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-01-21
     * @version     v1.0
     *
     * @return  是否成功
     */
    public boolean close();
    
    
    
    /**
     * 按配置参数初始化Modbus运行环境
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-20
     * @version     v1.0
     *
     * @return      是否初始化成功
     */
    public boolean init();
    
    
    
    /**
     * 读取输出线圈（功能码0x01）。即，读取[01 Coil Status 0x]类型 开关数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-20
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Offset    偏移位置，即相对地址。下标从 0 开始
     * @return            异常时返回null
     */
    public Boolean readOutputStatus(int i_SlaveID, int i_Offset);
    
    
    
    /**
     * 批量读取输出线圈（功能码0x01）。即，读取[01 Coil Status 0x]类型 开关数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-24
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Offset    偏移位置，即相对地址。下标从 0 开始
     * @param i_DataCount 读取的数据数量
     * @return            异常时返回null
     */
    public Boolean [] readOutputStatus(int i_SlaveID, int i_Offset, int i_DataCount);
    
    
    
    /**
     * 批量读取输出线圈（功能码0x01）。即，读取[01 Coil Status 0x]类型 开关数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-26
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Datagram  数据报文
     * @return            异常时返回null
     */
    public Map<String ,Object> readOutputStatus(int i_SlaveID ,List<? extends MDataItem> i_Datagram);
    
    
    
    /**
     * 读取输入线圈（功能码0x02）。即，读取[02 Input Status 1x]类型 开关数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-20
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Offset    偏移位置，即相对地址。下标从 0 开始
     * @return            异常时返回null
     */
    public Boolean readInputStatus(int i_SlaveID, int i_Offset);
    
    
    
    /**
     * 批量读取输入线圈（功能码0x02）。即，读取[02 Input Status 1x]类型 开关数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-24
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Offset    偏移位置，即相对地址。下标从 0 开始
     * @param i_DataCount 读取的数据数量
     * @return            异常时返回null
     */
    public Boolean [] readInputStatus(int i_SlaveID, int i_Offset, int i_DataCount);
    
    
    
    /**
     * 批量读取输入线圈（功能码0x02）。即，读取[02 Input Status 1x]类型 开关数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-26
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Datagram  数据项集合
     * @return            异常时返回null
     */
    public Map<String ,Object> readInputStatus(int i_SlaveID ,List<? extends MDataItem> i_Datagram);
    
    

    /**
     * 读取输出寄存器（功能码0x03）。即，读取[03 Holding Register类型 2x]模拟量数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-20
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Offset    偏移位置，即相对地址。下标从 0 开始
     * @param i_DataType  数据类型。参见 com.lps.microservice.modbus.ModbusData
     * @return            异常时返回null
     */
    public Number readOutputRegister(int i_SlaveID, int i_Offset, int i_DataType);
    
    
    
    /**
     * 批量读取输出寄存器（功能码0x03）。即，读取[03 Holding Register类型 2x]模拟量数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-24
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Offset    偏移位置，即相对地址。下标从 0 开始
     * @param i_DataType  数据类型。参见 com.lps.microservice.modbus.ModbusData
     * @param i_DataCount 读取的数据数量
     * @return            异常时返回null
     */
    public Number [] readOutputRegister(int i_SlaveID, int i_Offset, int i_DataType ,int i_DataCount);
    
    
    
    /**
     * 批量读取输出寄存器（功能码0x03）。即，读取[03 Holding Register类型 2x]模拟量数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-26
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Datagram  数据报文
     * @return            异常时返回null
     */
    public Map<String ,Object> readOutputRegister(int i_SlaveID ,List<? extends MDataItem> i_Datagram);
    
    
    
    /**
     * 读取输入寄存器（功能码0x04）。即，读取[04 Input Registers 3x]类型 模拟量数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-20
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Offset    偏移位置，即相对地址。下标从 0 开始
     * @param i_DataType  数据类型。参见 com.lps.microservice.modbus.ModbusData
     * @return            异常时返回null
     */
    public Number readInputRegister(int i_SlaveID, int i_Offset, int i_DataType);
    
    
    
    /**
     * 批量读取输入寄存器（功能码0x04）。即，读取[04 Input Registers 3x]类型 模拟量数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-24
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Offset    偏移位置，即相对地址。下标从 0 开始
     * @param i_DataType  数据类型。参见 com.lps.microservice.modbus.ModbusData
     * @param i_DataCount 读取的数据数量
     * @return            异常时返回null
     */
    public Number [] readInputRegister(int i_SlaveID, int i_Offset, int i_DataType ,int i_DataCount);
    
    
    
    /**
     * 批量读取输入寄存器（功能码0x04）。即，读取[04 Input Registers 3x]类型 模拟量数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-26
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Datagram  数据报文
     * @return            异常时返回null
     */
    public Map<String ,Object> readInputRegister(int i_SlaveID ,List<? extends MDataItem> i_Datagram);
    
}
