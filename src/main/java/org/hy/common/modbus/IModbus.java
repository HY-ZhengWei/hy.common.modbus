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
     * 读取输出线圈（功能码01）。即，读取[01 Coil Status 0x]类型 开关数据
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
     * 批量读取输出线圈（功能码01）。即，读取[01 Coil Status 0x]类型 开关数据
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
     * 批量读取输出线圈（功能码01）。即，读取[01 Coil Status 0x]类型 开关数据
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
     * 读取输入线圈（功能码02）。即，读取[02 Input Status 1x]类型 开关数据
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
     * 批量读取输入线圈（功能码02）。即，读取[02 Input Status 1x]类型 开关数据
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
     * 批量读取输入线圈（功能码02）。即，读取[02 Input Status 1x]类型 开关数据
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
     * 读取输出寄存器（功能码03）。即，读取[03 Holding Register类型 2x]模拟量数据
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
     * 批量读取输出寄存器（功能码03）。即，读取[03 Holding Register类型 2x]模拟量数据
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
     * 批量读取输出寄存器（功能码03）。即，读取[03 Holding Register类型 2x]模拟量数据
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
     * 读取输入寄存器（功能码04）。即，读取[04 Input Registers 3x]类型 模拟量数据
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
     * 批量读取输入寄存器（功能码04）。即，读取[04 Input Registers 3x]类型 模拟量数据
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
     * 批量读取输入寄存器（功能码04）。即，读取[04 Input Registers 3x]类型 模拟量数据
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
    
    
    
    /**
     * 写入单个线圈 (功能码05)。设置单个线圈为ON或OFF。位 (Bit)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-06-30
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Offset    偏移位置，即相对地址。下标从 0 开始
     * @param i_Data      写入数据
     * @return            返回成功结果
     */
    public boolean writeStatus(int i_SlaveID ,int i_Offset ,boolean i_Data);
    
    
    
    /**
     * 写入多个线圈 (功能码15)。设置多个连续线圈为ON或OFF。数据类型：位 (Bit)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-06-30
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Offset    偏移位置，即相对地址。下标从 0 开始
     * @param i_Datas     写入数据
     * @return            返回成功结果
     */
    public boolean writeStatuss(int i_SlaveID ,int i_Offset ,boolean [] i_Datas);
    
    
    
    /**
     * 写入单个保持寄存器 (功能码06)。设置单个保持寄存器的值。数据类型：字 (Word)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-06-30
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Offset    偏移位置，即相对地址。下标从 0 开始
     * @param i_Data      写入数据
     * @return            返回成功结果
     */
    public boolean writeRegister(int i_SlaveID ,int i_Offset ,int i_Data);
    
    
    
    /**
     * 写入多个保持寄存器 (功能码16)。设置多个保持寄存器的值。数据类型：字 (Word)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-06-30
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Offset    偏移位置，即相对地址。下标从 0 开始
     * @param i_Data      写入数据
     * @return            返回成功结果
     */
    public boolean writeRegisters(int i_SlaveID ,int i_Offset ,short [] i_Datas);
    
    
    
    /**
     * 写入多个保持寄存器 (功能码16)。设置多个保持寄存器的值。数据类型：字 (Word)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-06-30
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Offset    偏移位置，即相对地址。下标从 0 开始
     * @param i_Data      写入数据
     * @return            返回成功结果
     */
    public boolean writeString(int i_SlaveID ,int i_Offset ,String i_Datas);
    
    
    
    /**
     * 写入多个保持寄存器 (功能码16)。设置多个保持寄存器的值。数据类型：字 (Word)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-07-02
     * @version     v1.0
     *
     * @param i_SlaveID       从站编号。下标从 1 开始
     * @param i_Offset        偏移位置，即相对地址。下标从 0 开始
     * @param i_Data          写入数据
     * @param i_CharEncoding  数据的字符集编码
     * @return                返回成功结果
     */
    public boolean writeString(int i_SlaveID ,int i_Offset ,String i_Datas ,String i_CharEncoding);
    
    
    
    /**
     * 写入数据报文（偏移量可以不连续）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-07-01
     * @version     v1.0
     *
     * @param i_SlaveID     从站编号。下标从 1 开始
     * @param io_Datagram   数据报文
     * @return
     */
    public boolean writes(int i_SlaveID ,List<? extends MDataItem> io_Datagram);
    
    
    
    /**
     * 写入数据报文（偏移量可以不连续）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-07-15
     * @version     v1.0
     *
     * @param i_SlaveID     从站编号。下标从 1 开始
     * @param io_Datagram   数据报文
     * @param i_OffsetSort  是否按偏移量排序。默认为：false，按用户指定的报文集合顺序
     * @return
     */
    public boolean writes(int i_SlaveID ,List<? extends MDataItem> io_Datagram ,boolean i_OffsetSort);
    
}
