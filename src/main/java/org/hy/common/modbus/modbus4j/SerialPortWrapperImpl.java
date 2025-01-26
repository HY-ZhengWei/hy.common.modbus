package org.hy.common.modbus.modbus4j;

import org.hy.common.hart.serialPort.SerialPortConfig;
import org.hy.common.hart.serialPort.SerialPortWrapperDefault;

import com.serotonin.modbus4j.serial.SerialPortWrapper;





/**
 * 串口包装类
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-01-17
 * @version     v1.0
 */
public class SerialPortWrapperImpl extends SerialPortWrapperDefault implements SerialPortWrapper
{

    public SerialPortWrapperImpl(SerialPortConfig i_Config)
    {
        super(i_Config);
    }
    
}
