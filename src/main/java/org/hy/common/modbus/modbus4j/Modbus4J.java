package org.hy.common.modbus.modbus4j;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hy.common.ByteHelp;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.hart.serialPort.SerialPortFactory;
import org.hy.common.modbus.IModbus;
import org.hy.common.modbus.data.MConnConfig;
import org.hy.common.modbus.data.MDataItem;
import org.hy.common.modbus.enums.ModbusData;
import org.hy.common.modbus.enums.ModbusProtocol;
import org.hy.common.modbus.enums.ModbusType;
import org.hy.common.xml.log.Logger;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.WriteCoilRequest;
import com.serotonin.modbus4j.msg.WriteCoilResponse;
import com.serotonin.modbus4j.msg.WriteCoilsRequest;
import com.serotonin.modbus4j.msg.WriteCoilsResponse;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;





/**
 * Modbus4j的类库实现
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-01-20
 * @version     v1.0
 *              v2.0  2025-06-30  添加：write系列方法
 *              v3.0  2025-07-15  添加：reads方法，按数据报文中数据项的类型，从两个常用的输出类型批量读取数据
 */
public class Modbus4J implements IModbus
{
    
    private static final Logger                          $Logger                 = new Logger(Modbus4J.class);
    
    /** 批量读取线圈的缓存。因为实现生产中，是重复读取相同类型的数据，就可以避免大量构造重复对象 */
    private static final Map<String ,BatchRead<Integer>> $BatchReadStatusCache   = new HashMap<String ,BatchRead<Integer>>();
    
    /** 批量读取寄存器的缓存。因为实现生产中，是重复读取相同类型的数据，就可以避免大量构造重复对象 */
    private static final Map<String ,BatchRead<Number>>  $BatchReadRegisterCache = new HashMap<String ,BatchRead<Number>>();
    
    private static ModbusFactory                         $ModbusFactory;
    
    
    
    
    private static synchronized ModbusFactory getModbusFactory()
    {
        if ( $ModbusFactory == null )
        {
            $ModbusFactory = new ModbusFactory();
            Modbus4JData.init();
        }
        
        return $ModbusFactory;
    }
    
    
    
    /** 是否初始，并初始化成功 */
    private boolean      isInit;
    
    /** 配置参数 */
    private MConnConfig  config;
    
    /** 主站 */
    private ModbusMaster master;
    
    
    
    public Modbus4J(MConnConfig i_MConnConfig)
    {
        this.config = i_MConnConfig;
    }
    
    
    
    /**
     * 关闭连接，释放资源
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-01-21
     * @version     v1.0
     *
     */
    public synchronized boolean close()
    {
        boolean v_Ret = true;
        
        if ( this.master != null )
        {
            try
            {
                if ( this.isInit )
                {
                    this.master.destroy();
                    $Logger.info("关闭连接成功：" + this.config.toString());
                }
                else
                {
                    $Logger.info("连接未曾打开：" + this.config.toString());
                }
            }
            catch (Exception exce)
            {
                $Logger.error("关闭连接异常：" + this.config.toString() ,exce);
            }
            
            this.master = null;
            this.isInit = false;
            v_Ret = false;
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 按配置参数初始化Modbus运行环境
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-20
     * @version     v1.0
     *
     * @return      是否初始化成功
     */
    @Override
    public synchronized boolean init()
    {
        if ( this.isInit )
        {
            return true;
        }
        
        if ( ModbusType.Master.equals(this.config.getType()) )
        {
            if ( ModbusProtocol.TCP.equals(this.config.getProtocol()) )
            {
                IpParameters v_Params = new IpParameters();
                v_Params.setHost(this.config.getHost());
                v_Params.setPort(this.config.getPort());
                this.master = getModbusFactory().createTcpMaster(v_Params ,false);
            }
            else if ( ModbusProtocol.UDP.equals(this.config.getProtocol()) )
            {
                IpParameters v_Params = new IpParameters();
                v_Params.setHost(this.config.getHost());
                v_Params.setPort(this.config.getPort());
                this.master = getModbusFactory().createUdpMaster(v_Params);
            }
            else if ( ModbusProtocol.RTU.equals(this.config.getProtocol()) )
            {
                this.master = getModbusFactory().createRtuMaster(SerialPortFactory.get(this.config ,SerialPortWrapperImpl.class));
            }
            else if ( ModbusProtocol.ASCII.equals(this.config.getProtocol()) )
            {
                this.master = getModbusFactory().createAsciiMaster(SerialPortFactory.get(this.config ,SerialPortWrapperImpl.class));
            }
            
            try
            {
                this.master.setTimeout(Help.NVL(this.config.getTimeout() ,500));
                this.master.init();
                $Logger.info("打开连接成功：" + this.config.toString());
            }
            catch (Exception exce)
            {
                $Logger.error(exce);
                return false;
            }
        }
        else if ( ModbusType.Slave.equals(this.config.getType()) )
        {
            // TODO 用到时再实现
        }
        else
        {
            $Logger.error("Modbus type is invalid.");
            return false;
        }
        
        this.isInit = true;
        return true;
    }
    
    
    
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
    @Override
    public Boolean readOutputStatus(int i_SlaveID, int i_Offset)
    {
        BaseLocator<Boolean> v_BaseLocator = BaseLocator.coilStatus(i_SlaveID, i_Offset);
        
        try
        {
            return this.master.getValue(v_BaseLocator);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
    
    
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
    @Override
    public Boolean [] readOutputStatus(int i_SlaveID, int i_Offset, int i_DataCount)
    {
        BatchRead<Integer> v_BatchRead = null;
        synchronized (this)
        {
            String v_BatchKey = i_SlaveID + "_" + i_Offset + "_" + i_DataCount;
            
            v_BatchRead = $BatchReadStatusCache.get(v_BatchKey);
            if ( v_BatchRead == null )
            {
                v_BatchRead = new BatchRead<Integer>();
                
                for (int x=0; x<i_DataCount; x++)
                {
                    v_BatchRead.addLocator(x, BaseLocator.coilStatus(i_SlaveID, i_Offset));
                }
                
                $BatchReadStatusCache.put(v_BatchKey ,v_BatchRead);
            }
        }
        
        try
        {
            BatchResults<Integer> v_BatchRet = this.master.send(v_BatchRead);
            Boolean []            v_Ret      = new Boolean[i_DataCount];
            for (int x=0; x<i_DataCount; x++)
            {
                v_Ret[x] = v_BatchRet.getIntValue(x) != 0;
            }
            
            return v_Ret;
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
    
    
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
    @Override
    public Map<String ,Object> readOutputStatus(int i_SlaveID ,List<? extends MDataItem> i_Datagram)
    {
        BatchRead<Integer> v_BatchRead = null;
        synchronized (this)
        {
            String v_BatchKey = i_SlaveID + "_HC_" + i_Datagram.hashCode();
            
            v_BatchRead = $BatchReadStatusCache.get(v_BatchKey);
            if ( v_BatchRead == null )
            {
                v_BatchRead = new BatchRead<Integer>();
                
                for (int x=0; x<i_Datagram.size(); x++)
                {
                    MDataItem v_DataItem = i_Datagram.get(x);
                    v_BatchRead.addLocator(x, BaseLocator.coilStatus(i_SlaveID, v_DataItem.getOffset()));
                }
                
                $BatchReadStatusCache.put(v_BatchKey ,v_BatchRead);
            }
        }
        
        try
        {
            BatchResults<Integer> v_BatchRet = this.master.send(v_BatchRead);
            Map<String ,Object>   v_Ret      = new LinkedHashMap<String ,Object>();
            for (int x=0; x<i_Datagram.size(); x++)
            {
                v_Ret.put(i_Datagram.get(x).getName() ,v_BatchRet.getIntValue(x) != 0 ? 1 : 0);
            }
            
            return v_Ret;
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
    
    
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
    @Override
    public Boolean readInputStatus(int i_SlaveID, int i_Offset)
    {
        BaseLocator<Boolean> v_BaseLocator = BaseLocator.inputStatus(i_SlaveID, i_Offset);
        try
        {
            return this.master.getValue(v_BaseLocator);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
    
    
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
    @Override
    public Boolean [] readInputStatus(int i_SlaveID, int i_Offset, int i_DataCount)
    {
        BatchRead<Integer> v_BatchRead = null;
        synchronized (this)
        {
            String v_BatchKey = i_SlaveID + "_" + i_Offset + "_" + i_DataCount;
            
            v_BatchRead = $BatchReadStatusCache.get(v_BatchKey);
            if ( v_BatchRead == null )
            {
                v_BatchRead = new BatchRead<Integer>();
                
                for (int x=0; x<i_DataCount; x++)
                {
                    v_BatchRead.addLocator(x, BaseLocator.inputStatus(i_SlaveID, i_Offset));
                }
                
                $BatchReadStatusCache.put(v_BatchKey ,v_BatchRead);
            }
        }
        
        try
        {
            BatchResults<Integer> v_BatchRet = this.master.send(v_BatchRead);
            Boolean []            v_Ret      = new Boolean[i_DataCount];
            for (int x=0; x<i_DataCount; x++)
            {
                v_Ret[x] = v_BatchRet.getIntValue(x) != 0;
            }
            
            return v_Ret;
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
    
    
    /**
     * 批量读取输入线圈（功能码02）。即，读取[02 Input Status 1x]类型 开关数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-26
     * @version     v1.0
     *
     * @param i_SlaveID   从站编号。下标从 1 开始
     * @param i_Datagram  数据报文
     * @return            异常时返回null
     */
    @Override
    public Map<String ,Object> readInputStatus(int i_SlaveID ,List<? extends MDataItem> i_Datagram)
    {
        BatchRead<Integer> v_BatchRead = null;
        synchronized (this)
        {
            String v_BatchKey = i_SlaveID + "_HC_" + i_Datagram.hashCode();
            
            v_BatchRead = $BatchReadStatusCache.get(v_BatchKey);
            if ( v_BatchRead == null )
            {
                v_BatchRead = new BatchRead<Integer>();
                
                for (int x=0; x<i_Datagram.size(); x++)
                {
                    MDataItem v_DataItem = i_Datagram.get(x);
                    v_BatchRead.addLocator(x, BaseLocator.inputStatus(i_SlaveID ,v_DataItem.getOffset()));
                }
                
                $BatchReadStatusCache.put(v_BatchKey ,v_BatchRead);
            }
        }
        
        try
        {
            BatchResults<Integer> v_BatchRet = this.master.send(v_BatchRead);
            Map<String ,Object>   v_Ret      = new LinkedHashMap<String ,Object>();
            for (int x=0; x<i_Datagram.size(); x++)
            {
                v_Ret.put(i_Datagram.get(x).getName() ,v_BatchRet.getIntValue(x) != 0 ? 1 : 0);
            }
            
            return v_Ret;
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
    

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
    @Override
    public Number readOutputRegister(int i_SlaveID, int i_Offset, int i_DataType)
    {
        BaseLocator<Number> v_BaseLocator = BaseLocator.holdingRegister(i_SlaveID ,i_Offset ,Modbus4JData.toDataType(i_DataType));
        try
        {
            return this.master.getValue(v_BaseLocator);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
    
    
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
    @Override
    public Number [] readOutputRegister(int i_SlaveID, int i_Offset, int i_DataType ,int i_DataCount)
    {
        BatchRead<Number> v_BatchRead = null;
        synchronized (this)
        {
            String v_BatchKey = i_SlaveID + "_" + i_Offset + "_" + i_DataType + "_" + i_DataCount;
            
            v_BatchRead = $BatchReadRegisterCache.get(v_BatchKey);
            if ( v_BatchRead == null )
            {
                v_BatchRead = new BatchRead<Number>();
                
                for (int x=0; x<i_DataCount; x++)
                {
                    v_BatchRead.addLocator(x, BaseLocator.holdingRegister(i_SlaveID ,i_Offset + x ,Modbus4JData.toDataType(i_DataType)));
                }
                
                $BatchReadRegisterCache.put(v_BatchKey ,v_BatchRead);
            }
        }
        
        try
        {
            BatchResults<Number> v_BatchRet = this.master.send(v_BatchRead);
            Number []            v_Ret      = new Number[i_DataCount];
            for (int x=0; x<i_DataCount; x++)
            {
                v_Ret[x] = (Number) v_BatchRet.getValue(x);
            }
            
            return v_Ret;
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
    
    
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
    @Override
    public Map<String ,Object> readOutputRegister(int i_SlaveID ,List<? extends MDataItem> i_Datagram)
    {
        BatchRead<Number> v_BatchRead = null;
        synchronized (this)
        {
            String v_BatchKey = i_SlaveID + "_HC_" + i_Datagram.hashCode();
            
            v_BatchRead = $BatchReadRegisterCache.get(v_BatchKey);
            if ( v_BatchRead == null )
            {
                v_BatchRead = new BatchRead<Number>();
                
                for (int x=0; x<i_Datagram.size(); x++)
                {
                    MDataItem v_DataItem = i_Datagram.get(x);
                    v_BatchRead.addLocator(x, BaseLocator.holdingRegister(i_SlaveID ,v_DataItem.getOffset() ,Modbus4JData.toDataType(v_DataItem.getDataType().getValue())));
                }
                
                $BatchReadRegisterCache.put(v_BatchKey ,v_BatchRead);
            }
        }
        
        try
        {
            BatchResults<Number> v_BatchRet = this.master.send(v_BatchRead);
            Map<String ,Object>  v_Ret      = new LinkedHashMap<String ,Object>();
            for (int x=0; x<i_Datagram.size(); x++)
            {
                v_Ret.put(i_Datagram.get(x).getName() ,v_BatchRet.getValue(x));
            }
            
            return v_Ret;
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
    
    
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
    @Override
    public Number readInputRegister(int i_SlaveID, int i_Offset, int i_DataType)
    {
        BaseLocator<Number> v_BaseLocator = BaseLocator.inputRegister(i_SlaveID, i_Offset, Modbus4JData.toDataType(i_DataType));
        try
        {
            return this.master.getValue(v_BaseLocator);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
    
    
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
    @Override
    public Number [] readInputRegister(int i_SlaveID, int i_Offset, int i_DataType ,int i_DataCount)
    {
        BatchRead<Number> v_BatchRead = null;
        synchronized (this)
        {
            String v_BatchKey = i_SlaveID + "_" + i_Offset + "_" + i_DataType + "_" + i_DataCount;
            
            v_BatchRead = $BatchReadRegisterCache.get(v_BatchKey);
            if ( v_BatchRead == null )
            {
                v_BatchRead = new BatchRead<Number>();
                
                for (int x=0; x<i_DataCount; x++)
                {
                    v_BatchRead.addLocator(x, BaseLocator.inputRegister(i_SlaveID ,i_Offset + x ,Modbus4JData.toDataType(i_DataType)));
                }
                
                $BatchReadRegisterCache.put(v_BatchKey ,v_BatchRead);
            }
        }
        
        try
        {
            BatchResults<Number> v_BatchRet = this.master.send(v_BatchRead);
            Number []            v_Ret      = new Number[i_DataCount];
            for (int x=0; x<i_DataCount; x++)
            {
                v_Ret[x] = (Number) v_BatchRet.getValue(x);
            }
            
            return v_Ret;
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
    
    
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
    @Override
    public Map<String ,Object> readInputRegister(int i_SlaveID ,List<? extends MDataItem> i_Datagram)
    {
        BatchRead<Number> v_BatchRead = null;
        synchronized (this)
        {
            String v_BatchKey = i_SlaveID + "_HC_" + i_Datagram.hashCode();
            
            v_BatchRead = $BatchReadRegisterCache.get(v_BatchKey);
            if ( v_BatchRead == null )
            {
                v_BatchRead = new BatchRead<Number>();
                
                for (int x=0; x<i_Datagram.size(); x++)
                {
                    MDataItem v_DataItem = i_Datagram.get(x);
                    v_BatchRead.addLocator(x, BaseLocator.inputRegister(i_SlaveID ,v_DataItem.getOffset() ,Modbus4JData.toDataType(v_DataItem.getDataType().getValue())));
                }
                
                $BatchReadRegisterCache.put(v_BatchKey ,v_BatchRead);
            }
        }
        
        try
        {
            BatchResults<Number> v_BatchRet = this.master.send(v_BatchRead);
            Map<String ,Object>  v_Ret      = new LinkedHashMap<String ,Object>();
            for (int x=0; x<i_Datagram.size(); x++)
            {
                v_Ret.put(i_Datagram.get(x).getName() ,v_BatchRet.getValue(x));
            }
            
            return v_Ret;
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return null;
    }
    
    
    
    /**
     * 按数据报文中数据项的类型，从两个常用的输出类型批量读取数据。
     * 
     *   分别从下面两个输出类型批量读取数据
     *   1. Boolean类型，从输出线圈  （功能码01）批量读取数据。即，读取[01 Coil Status 0x]类型 开关数据
     *   2. Integer类型，从输出寄存器（功能码03）批量读取数据。即，读取[03 Holding Register类型 2x]模拟量数据
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-07-15
     * @version     v1.0
     *
     * @param i_SlaveID
     * @param i_Datagram
     * @return
     */
    public Map<String ,Object> reads(int i_SlaveID ,List<? extends MDataItem> i_Datagram)
    {
        if ( Help.isNull(i_Datagram) )
        {
            return null;
        }
        
        List<MDataItem> v_DatagramBoolean = new ArrayList<MDataItem>();
        List<MDataItem> v_DatagramInteger = new ArrayList<MDataItem>();
        for (MDataItem v_MDItem : i_Datagram)
        {
            if ( ModbusData.D1Bit.equals(v_MDItem.getDataType()) )
            {
                v_DatagramBoolean.add(v_MDItem);
            }
            else
            {
                v_DatagramInteger.add(v_MDItem);
            }
        }
        
        Map<String ,Object> v_RetBoolean = null;
        Map<String ,Object> v_RetInteger = null;
        if ( !Help.isNull(v_DatagramBoolean) )
        {
            v_RetBoolean = this.readOutputStatus(i_SlaveID ,v_DatagramBoolean);
        }
        if ( !Help.isNull(v_DatagramInteger) )
        {
            v_RetInteger = this.readOutputRegister(i_SlaveID ,v_DatagramInteger);
        }
        
        if ( Help.isNull(v_RetBoolean) && Help.isNull(v_DatagramInteger) )
        {
            return new LinkedHashMap<String ,Object>();
        }
        else if ( Help.isNull(v_RetBoolean) )
        {
            return v_RetInteger;
        }
        else if ( Help.isNull(v_RetInteger) )
        {
            return v_RetBoolean;
        }
        else
        {
            v_RetBoolean.putAll(v_RetInteger);
            v_RetInteger.clear();
            return v_RetBoolean;
        }
    }
    
    
    
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
    public boolean writeStatus(int i_SlaveID ,int i_Offset ,boolean i_Data)  
    {
        try
        {
            WriteCoilRequest  v_Request  = new WriteCoilRequest(i_SlaveID, i_Offset, i_Data);
            WriteCoilResponse v_Response = (WriteCoilResponse) this.master.send(v_Request);
            return !v_Response.isException();
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return false;
    }
    

    
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
    public boolean writeStatuss(int i_SlaveID ,int i_Offset ,boolean [] i_Datas)  
    {
        try
        {
            WriteCoilsRequest  v_Request  = new WriteCoilsRequest(i_SlaveID, i_Offset, i_Datas);
            WriteCoilsResponse v_Response = (WriteCoilsResponse) this.master.send(v_Request);
            return !v_Response.isException();
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return false;
    }
    
    
    
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
    public boolean writeRegister(int i_SlaveID ,int i_Offset ,int i_Data) 
    {
        try
        {
            WriteRegisterRequest  v_Request  = new WriteRegisterRequest(i_SlaveID, i_Offset, i_Data);
            WriteRegisterResponse v_Response = (WriteRegisterResponse) this.master.send(v_Request);
            
            return !v_Response.isException();
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return false;
    }
    
    
    
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
    public boolean writeRegisters(int i_SlaveID ,int i_Offset ,short [] i_Datas) 
    {
        try
        {
            WriteRegistersRequest  v_Request  = new WriteRegistersRequest(i_SlaveID, i_Offset, i_Datas);
            WriteRegistersResponse v_Response = (WriteRegistersResponse) this.master.send(v_Request);
            
            return !v_Response.isException();
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return false;
    }
    
    
    
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
    public boolean writeString(int i_SlaveID ,int i_Offset ,String i_Datas)
    {
        return this.writeString(i_SlaveID ,i_Offset ,i_Datas ,"GBK");
    }
    
    
    
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
    public boolean writeString(int i_SlaveID ,int i_Offset ,String i_Datas ,String i_CharEncoding) 
    {
        if ( i_Datas == null )
        {
            return false;
        }
        
        try
        {
            byte   [] v_Bytes  = i_Datas.getBytes(Charset.forName(i_CharEncoding));
            String    v_Hexs   = ByteHelp.bytesToHex(v_Bytes);
            int       v_PadLen = v_Hexs.length() % 4; 
            if ( v_PadLen > 0 )
            {
                v_Hexs = v_Hexs + StringHelp.lpad("" ,v_PadLen ,"0");
            }
            String [] v_HexArr = StringHelp.splitToArray(v_Hexs ,4);
            
            for (int i=0; i<v_HexArr.length; i++) 
            {
                String v_Hex  = v_HexArr[i];
                short  v_Data = (short) Integer.parseInt(v_Hex, 16);
                BaseLocator<Number> v_Locator = BaseLocator.holdingRegister(i_SlaveID ,i_Offset + i ,ModbusData.D2B_Int_Unsigned.getValue());
                this.master.setValue(v_Locator, v_Data);
            }
            
            return true;
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return false;
    }
    
    
    
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
    public boolean writes(int i_SlaveID ,List<? extends MDataItem> io_Datagram)
    {
        return this.writes(i_SlaveID ,io_Datagram ,false);
    }
    
    
    
    /**
     * 写入数据报文（偏移量可以不连续）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-07-01
     * @version     v1.0
     *
     * @param i_SlaveID     从站编号。下标从 1 开始
     * @param io_Datagram   数据报文
     * @param i_OffsetSort  是否按偏移量排序。默认为：false，按用户指定的报文集合顺序
     * @return
     */
    public boolean writes(int i_SlaveID ,List<? extends MDataItem> io_Datagram ,boolean i_OffsetSort)
    {
        if ( Help.isNull(io_Datagram) )
        {
            return false;
        }
        
        if ( i_OffsetSort )
        {
            // 将地址排序
            Help.toSort(io_Datagram ,"offset");
        }
        
        for (int x=0; x<io_Datagram.size(); x++)
        {
            MDataItem v_DataItem = io_Datagram.get(x);
            
            if ( v_DataItem.getDataType() == null )
            {
                return false;
            }
            else if ( v_DataItem.getData() == null || v_DataItem.getData().length() <= 0 )
            {
                return false;
            }
            else if ( v_DataItem.getOffset() == null || v_DataItem.getOffset() < 0 )
            {
                return false;
            }
            else if ( ModbusData.D1B_Int_Unsigned_Lower.equals(v_DataItem.getDataType()) )
            {
                v_DataItem.setData(v_DataItem.getData().toLowerCase());
            }
            else if ( ModbusData.D1B_Int_Unsigned_Upper.equals(v_DataItem.getDataType()) )
            {
                v_DataItem.setData(v_DataItem.getData().toUpperCase());
            }
            else if ( ModbusData.DVarchar.equals(v_DataItem.getDataType()) )
            {
                // Nothing.
            }
            else if ( ModbusData.DChar.equals(v_DataItem.getDataType()) )
            {
                // Nothing.
            }
            else if ( ModbusData.D1Bit.equals(v_DataItem.getDataType()) )
            {
                Boolean.parseBoolean(v_DataItem.getData());
            }
            else
            {
                if ( !Help.isNumber(v_DataItem.getData()) )
                {
                    return false;
                }
            }
        }
        
        try
        {
            for (int x=0; x<io_Datagram.size(); x++)
            {
                MDataItem v_DataItem = io_Datagram.get(x);
                Number    v_Data     = null;
                
                if ( v_DataItem.getDataType().equals(ModbusData.D1Bit) )
                {
                    v_Data = Boolean.parseBoolean(v_DataItem.getData()) ? 1 : 0;
                }
                else if ( v_DataItem.getDataType().equals(ModbusData.D2B_Int_Unsigned) 
                       || v_DataItem.getDataType().equals(ModbusData.D2B_Int_Signed) 
                       || v_DataItem.getDataType().equals(ModbusData.D2B_Int_Unsigned_Swapped) 
                       || v_DataItem.getDataType().equals(ModbusData.D2B_Int_Signed_Swapped) 
                       || v_DataItem.getDataType().equals(ModbusData.D4B_Int_Unsigned) 
                       || v_DataItem.getDataType().equals(ModbusData.D4B_Int_Signed) 
                       || v_DataItem.getDataType().equals(ModbusData.D4B_Int_Unsigned_Swapped) 
                       || v_DataItem.getDataType().equals(ModbusData.D4B_Int_Signed_Swapped) 
                       || v_DataItem.getDataType().equals(ModbusData.D4B_Int_Unsigned_Swapped_Swapped) 
                       || v_DataItem.getDataType().equals(ModbusData.D4B_Int_Signed_Swapped_Swapped) )
                {
                    v_Data = Integer.parseInt(v_DataItem.getData());
                }
                else if ( v_DataItem.getDataType().equals(ModbusData.D8B_Int_Unsigned) 
                       || v_DataItem.getDataType().equals(ModbusData.D8B_Int_Signed)
                       || v_DataItem.getDataType().equals(ModbusData.D8B_Int_Unsigned_Swapped)
                       || v_DataItem.getDataType().equals(ModbusData.D8B_Int_Signed_Swapped) )
                {
                    v_Data = Long.parseLong(v_DataItem.getData());
                }
                else if ( v_DataItem.getDataType().equals(ModbusData.D4B_Float) 
                       || v_DataItem.getDataType().equals(ModbusData.D4B_Float_Swapped)
                       || v_DataItem.getDataType().equals(ModbusData.D4B_Float_Swapped_INVERTED) )
                {
                    v_Data = Float.parseFloat(v_DataItem.getData());
                }
                else if ( v_DataItem.getDataType().equals(ModbusData.D8B_Float) 
                       || v_DataItem.getDataType().equals(ModbusData.D8B_Float_Swapped)
                       || v_DataItem.getDataType().equals(ModbusData.D4B_Float_Swapped_INVERTED)
                       || v_DataItem.getDataType().equals(ModbusData.D2B_BCD)
                       || v_DataItem.getDataType().equals(ModbusData.D4B_BCD)
                       || v_DataItem.getDataType().equals(ModbusData.D4B_BCD_Swapped) )
                {
                    v_Data = Double.parseDouble(v_DataItem.getData());
                }
                else if ( v_DataItem.getDataType().equals(ModbusData.DChar) 
                       || v_DataItem.getDataType().equals(ModbusData.D1B_Int_Unsigned_Lower)
                       || v_DataItem.getDataType().equals(ModbusData.D1B_Int_Unsigned_Upper) )
                {
                    v_Data = (int) v_DataItem.getData().charAt(0);
                }
                else if ( v_DataItem.getDataType().equals(ModbusData.DVarchar) )
                {
                    if ( this.writeString(i_SlaveID ,v_DataItem.getOffset() ,v_DataItem.getData()) )
                    {
                        continue;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
                
                BaseLocator<Number> v_Locator = BaseLocator.holdingRegister(i_SlaveID ,v_DataItem.getOffset() ,v_DataItem.getDataType().getValue());
                this.master.setValue(v_Locator ,v_Data);
            }
            
            return true;
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        return false;
    }
    
    
    
    /**
     * 写入数据报文（偏移量可以不连续）
     * 
     * 尚不完善，谨慎使用
     * 
     * 因为不是所有Modbus设备都支持功能码23，并且类库也没有直接支持功能码23（不连续读写操作），
     * 因此，用间接的方式实现（模拟不连续写入）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-07-01
     * @version     v1.0
     *
     * @param i_SlaveID     从站编号。下标从 1 开始
     * @param io_Datagram   数据报文
     * @return
     */
    public boolean writes2(int i_SlaveID ,List<? extends MDataItem> io_Datagram)
    {
        if ( Help.isNull(io_Datagram) )
        {
            return false;
        }
        
        // 将地址排序
        Help.toSort(io_Datagram ,"offset");
        
        for (int x=0; x<io_Datagram.size(); x++)
        {
            MDataItem v_DataItem = io_Datagram.get(x);
            
            if ( v_DataItem.getDataType() == null )
            {
                return false;
            }
            else if ( v_DataItem.getData() == null || v_DataItem.getData().length() <= 0 )
            {
                return false;
            }
            else if ( v_DataItem.getOffset() == null || v_DataItem.getOffset() < 0 )
            {
                return false;
            }
            else if ( ModbusData.D1B_Int_Unsigned_Lower.equals(v_DataItem.getDataType()) )
            {
                v_DataItem.setData(v_DataItem.getData().toLowerCase());
            }
            else if ( ModbusData.D1B_Int_Unsigned_Upper.equals(v_DataItem.getDataType()) )
            {
                v_DataItem.setData(v_DataItem.getData().toUpperCase());
            }
            else if ( ModbusData.DVarchar.equals(v_DataItem.getDataType()) )
            {
                // Nothing.
            }
            else if ( ModbusData.DChar.equals(v_DataItem.getDataType()) )
            {
                // Nothing.
            }
            else if ( ModbusData.D1Bit.equals(v_DataItem.getDataType()) )
            {
                Boolean.parseBoolean(v_DataItem.getData());
            }
            else
            {
                if ( !Help.isNumber(v_DataItem.getData()) )
                {
                    return false;
                }
            }
        }
        
        for (int x=0; x<io_Datagram.size(); x++)
        {
            MDataItem v_DataItem = io_Datagram.get(x);
            boolean   v_Ret      = false;
            
            if ( ModbusData.DVarchar.equals(v_DataItem.getDataType()) )
            {
                v_Ret = this.writeString(i_SlaveID ,v_DataItem.getOffset() ,v_DataItem.getData());
            }
            else if ( ModbusData.DChar.equals(v_DataItem.getDataType()) )
            {
                v_Ret = this.writeRegister(i_SlaveID ,v_DataItem.getOffset() ,v_DataItem.getData().charAt(0));
            }
            else if ( ModbusData.D1Bit.equals(v_DataItem.getDataType()) )
            {
                v_Ret = this.writeStatus(i_SlaveID ,i_SlaveID ,Boolean.parseBoolean(v_DataItem.getData()));
            }
            else 
            {
                v_Ret = this.writeRegister(i_SlaveID ,v_DataItem.getOffset() ,Integer.parseInt(v_DataItem.getData()));
            }
            
            if ( !v_Ret )
            {
                return false;
            }
        }
        
        return true;
    }
    
}
