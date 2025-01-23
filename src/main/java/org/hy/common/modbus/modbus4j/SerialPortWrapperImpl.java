package org.hy.common.modbus.modbus4j;

import java.io.InputStream;
import java.io.OutputStream;

import org.hy.common.xml.log.Logger;

import com.fazecast.jSerialComm.SerialPort;
import com.serotonin.modbus4j.serial.SerialPortWrapper;





/**
 * 串口包装类
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-01-17
 * @version     v1.0
 */
public class SerialPortWrapperImpl implements SerialPortWrapper
{
    private static final Logger $Logger = new Logger(SerialPortWrapperImpl.class);
    
    
    
    /** 串口对象 */
    private SerialPort serialPort;
    
    /** 是否打开连接 */
    private boolean    isOpen;
    
    
    
    public SerialPortWrapperImpl(SerialPort i_SerialPort)
    {
        this.serialPort = i_SerialPort;
        this.isOpen     = this.serialPort.isOpen();
    }

    
    /**
     * 波特率
     *
     * @author      ZhengWei(HY)
     * @createDate  2025-01-17
     * @version     v1.0
     *
     * @return
     */
    @Override
    public int getBaudRate()
    {
        return this.serialPort.getBaudRate();
    }

    
    /**
     * 数据位
     *
     * @author      ZhengWei(HY)
     * @createDate  2025-01-17
     * @version     v1.0
     *
     * @return
     */
    @Override
    public int getDataBits()
    {
        return this.serialPort.getNumDataBits();
    }

    
    /**
     * 奇偶校验
     *
     * @author      ZhengWei(HY)
     * @createDate  2025-01-17
     * @version     v1.0
     *
     * @return
     */
    @Override
    public int getParity()
    {
        return this.serialPort.getParity();
    }

    
    /**
     * 停止位
     *
     * @author      ZhengWei(HY)
     * @createDate  2025-01-17
     * @version     v1.0
     *
     * @return
     */
    @Override
    public int getStopBits()
    {
        return this.serialPort.getNumStopBits();
    }
    
    
    /**
     * 读取串口流数据
     *
     * @author      ZhengWei(HY)
     * @createDate  2025-01-17
     * @version     v1.0
     *
     * @return
     */
    @Override
    public InputStream getInputStream()
    {
        synchronized ( this )
        {
            if ( !this.isOpen )
            {
                return null;
            }
        }
        return this.serialPort.getInputStream();
    }

    
    /**
     * 写入串口流数据 
     *
     * @author      ZhengWei(HY)
     * @createDate  2025-01-21
     * @version     v1.0
     *
     * @return
     */
    @Override
    public OutputStream getOutputStream()
    {
        synchronized ( this )
        {
            if ( !this.isOpen )
            {
                return null;
            }
        }
        return this.serialPort.getOutputStream();
    }
    
    
    /**
     * 打开串口设备的连接
     *
     * @author      ZhengWei(HY)
     * @createDate  2025-01-21
     * @version     v1.0
     *
     * @throws Exception
     */
    @Override
    public void open() throws Exception
    {
        try
        {
            if ( this.serialPort.openPort() )
            {
                this.isOpen = true;
                $Logger.info("打开串口设备成功：" + this.serialPort.getDescriptivePortName());
            }
            else
            {
                $Logger.error("打开串口设备失败：" + this.serialPort.getDescriptivePortName());
            }
        }
        catch (Exception exce)
        {
            $Logger.error("打开串口设备异常：" + this.serialPort.getDescriptivePortName() ,exce);
            throw exce;
        }
    }
    
    
    /**
     * 关闭串口设备的连接
     *
     * @author      ZhengWei(HY)
     * @createDate  2025-01-21
     * @version     v1.0
     *
     * @throws Exception
     */
    @Override
    public synchronized void close() throws Exception
    {
        try
        {
            if ( this.serialPort.closePort() )
            {
                this.isOpen = false;
                $Logger.info("关闭串口设备成功：" + this.serialPort.getDescriptivePortName());
            }
            else
            {
                $Logger.error("关闭串口设备失败：" + this.serialPort.getDescriptivePortName());
            }
        }
        catch (Exception exce)
        {
            $Logger.error("关闭串口设备异常：" + this.serialPort.getDescriptivePortName() ,exce);
            throw exce;
        }
    }

}
