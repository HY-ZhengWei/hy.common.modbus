package org.hy.common.modbus.data;

import java.io.Serializable;

import org.hy.common.modbus.enums.DataBit;
import org.hy.common.modbus.enums.ModbusProtocol;
import org.hy.common.modbus.enums.ModbusType;
import org.hy.common.modbus.enums.Parity;
import org.hy.common.modbus.enums.StopBit;





/**
 * Modbus协议配置
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-01-20
 * @version     v1.0
 */
public class MConnConfig implements Serializable
{
    
    private static final long serialVersionUID = -3779558159644911239L;
    
    

    /** IP地址 */
    private String         host;
    
    /** 访问端口 */
    private Integer        port;
    
    /** 协议类型 */
    private ModbusProtocol protocol;
    
    /** Modbus类型，主站or从站 */
    private ModbusType     type;
    
    /** 超时时长。单位：毫秒 */
    private Integer        timeout;
    
    /** 通讯串口名称 */
    private String         commPortName;
    
    /** 波特率 */
    private Integer        baudRate;
    
    /** 数据位 */
    private DataBit        dataBits;
    
    /** 停止位 */
    private StopBit        stopBit;
    
    /** 奇偶校验 */
    private Parity         parityCheck;
    
    
    
    /**
     * 获取：IP地址
     */
    public String getHost()
    {
        return host;
    }

    
    /**
     * 设置：IP地址
     * 
     * @param i_Host IP地址
     */
    public void setHost(String i_Host)
    {
        this.host = i_Host;
    }

    
    /**
     * 获取：访问端口
     */
    public Integer getPort()
    {
        return port;
    }

    
    /**
     * 设置：访问端口
     * 
     * @param i_Port 访问端口
     */
    public void setPort(Integer i_Port)
    {
        this.port = i_Port;
    }

    
    /**
     * 获取：协议类型
     */
    public ModbusProtocol getProtocol()
    {
        return protocol;
    }

    
    /**
     * 设置：协议类型
     * 
     * @param i_Protocol 协议类型
     */
    public void setProtocol(ModbusProtocol i_Protocol)
    {
        this.protocol = i_Protocol;
    }

    
    /**
     * 获取：Modbus类型，主站or从站
     */
    public ModbusType getType()
    {
        return type;
    }

    
    /**
     * 设置：Modbus类型，主站or从站
     * 
     * @param i_Type Modbus类型，主站or从站
     */
    public void setType(ModbusType i_Type)
    {
        this.type = i_Type;
    }
    

    /**
     * 获取：超时时长。单位：毫秒
     */
    public Integer getTimeout()
    {
        return timeout;
    }

    
    /**
     * 设置：超时时长。单位：毫秒
     * 
     * @param i_Timeout 超时时长。单位：毫秒
     */
    public void setTimeout(Integer i_Timeout)
    {
        this.timeout = i_Timeout;
    }

    
    /**
     * 获取：通讯串口名称
     */
    public String getCommPortName()
    {
        return commPortName;
    }

    
    /**
     * 设置：通讯串口名称
     * 
     * @param i_CommPortName 通讯串口名称
     */
    public void setCommPortName(String i_CommPortName)
    {
        this.commPortName = i_CommPortName;
    }


    /**
     * 获取：波特率
     */
    public Integer getBaudRate()
    {
        return baudRate;
    }

    
    /**
     * 设置：波特率
     * 
     * @param i_BaudRate 波特率
     */
    public void setBaudRate(Integer i_BaudRate)
    {
        this.baudRate = i_BaudRate;
    }

    
    /**
     * 获取：数据位
     */
    public DataBit getDataBits()
    {
        return dataBits;
    }

    
    /**
     * 设置：数据位
     * 
     * @param i_DataBits 数据位
     */
    public void setDataBits(DataBit i_DataBits)
    {
        this.dataBits = i_DataBits;
    }

    
    /**
     * 获取：停止位
     */
    public StopBit getStopBit()
    {
        return stopBit;
    }

    
    /**
     * 设置：停止位
     * 
     * @param i_StopBit 停止位
     */
    public void setStopBit(StopBit i_StopBit)
    {
        this.stopBit = i_StopBit;
    }

    
    /**
     * 获取：奇偶校验
     */
    public Parity getParityCheck()
    {
        return parityCheck;
    }

    
    /**
     * 设置：奇偶校验
     * 
     * @param i_ParityCheck 奇偶校验
     */
    public void setParityCheck(Parity i_ParityCheck)
    {
        this.parityCheck = i_ParityCheck;
    }


    @Override
    public String toString()
    {
        StringBuilder v_Builder = new StringBuilder();
        
        v_Builder.append(this.host).append(":");
        
        if ( ModbusProtocol.TCP.equals(this.protocol) || ModbusProtocol.UDP.equals(this.protocol) )
        {
            v_Builder.append(this.protocol.getValue()).append(":");
            v_Builder.append(this.port);
        }
        else if ( ModbusProtocol.RTU.equals(this.protocol) || ModbusProtocol.ASCII.equals(this.protocol) )
        {
            v_Builder.append(this.protocol.getValue()).append(":");
            v_Builder.append(this.commPortName);
        }
        else
        {
            v_Builder.append("::");
        }
        
        return v_Builder.toString();
    }
    
}
