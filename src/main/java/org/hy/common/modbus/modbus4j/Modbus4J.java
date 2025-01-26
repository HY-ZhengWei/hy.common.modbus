package org.hy.common.modbus.modbus4j;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.hart.serialPort.SerialPortFactory;
import org.hy.common.modbus.IModbus;
import org.hy.common.modbus.data.MConnConfig;
import org.hy.common.modbus.data.MDataItem;
import org.hy.common.modbus.enums.ModbusProtocol;
import org.hy.common.modbus.enums.ModbusType;
import org.hy.common.xml.log.Logger;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;





/**
 * Modbus4j的类库实现
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-01-20
 * @version     v1.0
 */
public class Modbus4J implements IModbus
{
    
    private static final Logger                          $Logger                 = new Logger(Modbus4J.class);
    
    /** 批量读取线圈的缓存。因为实现生产中，是重复读取相同类型的数据，就可以避免大量构造重复对象 */
    private static final Map<String ,BatchRead<Integer>> $BatchReadStatusCache  = new HashMap<String ,BatchRead<Integer>>();
    
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
     * 批量读取输入线圈（功能码0x02）。即，读取[02 Input Status 1x]类型 开关数据
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
    
}
