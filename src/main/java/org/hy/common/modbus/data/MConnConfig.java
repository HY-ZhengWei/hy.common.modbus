package org.hy.common.modbus.data;

import org.hy.common.Help;
import org.hy.common.hart.serialPort.SerialPortConfig;
import org.hy.common.modbus.enums.ModbusProtocol;
import org.hy.common.modbus.enums.ModbusType;





/**
 * Modbus协议配置
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-01-20
 * @version     v1.0
 *              v2.0  2025-08-28  添加：出现异常时，是否重新连接
 */
public class MConnConfig extends SerialPortConfig
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
    
    /** 出现异常时，是否重新连接。默认值：真 */
    private Integer        reconnect;
    
    /** 有效标记。1有效；-1无效 */
    private Integer        isValid;
    
    
    
    public MConnConfig()
    {
        this.reconnect = 1;
        this.isValid   = 1;
    }
    
    
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
     * 获取：出现异常时，是否重新连接。默认值：真
     */
    public Integer getReconnect()
    {
        return Help.NVL(this.reconnect ,1);
    }

    
    /**
     * 设置：出现异常时，是否重新连接。默认值：真
     * 
     * @param i_Reconnect 出现异常时，是否重新连接。默认值：真
     */
    public void setReconnect(Integer i_Reconnect)
    {
        this.reconnect = i_Reconnect;
    }

    
    /**
     * 获取：有效标记。1有效；-1无效
     */
    public Integer getIsValid()
    {
        return isValid;
    }

    
    /**
     * 设置：有效标记。1有效；-1无效
     * 
     * @param i_IsValid 有效标记。1有效；-1无效
     */
    public void setIsValid(Integer i_IsValid)
    {
        this.isValid = i_IsValid;
    }


    @Override
    public String toString()
    {
        StringBuilder v_Builder = new StringBuilder();
        
        if ( ModbusProtocol.TCP.equals(this.protocol) || ModbusProtocol.UDP.equals(this.protocol) )
        {
            v_Builder.append(Help.NVL(this.host ,"?")).append(":");
            v_Builder.append(this.protocol.getValue()).append(":");
            v_Builder.append(this.port);
        }
        else if ( ModbusProtocol.RTU.equals(this.protocol) || ModbusProtocol.ASCII.equals(this.protocol) )
        {
            v_Builder.append("127.0.0.1").append(":");
            v_Builder.append(this.protocol.getValue()).append(":");
            v_Builder.append(this.getCommPortName());
        }
        else
        {
            v_Builder.append("?:?:?");
        }
        
        return v_Builder.toString();
    }
    
}
