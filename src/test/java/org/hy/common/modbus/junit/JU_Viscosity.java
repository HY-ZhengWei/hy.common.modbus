package org.hy.common.modbus.junit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.hart.enums.DataBit;
import org.hy.common.hart.enums.FlowControl;
import org.hy.common.hart.enums.Parity;
import org.hy.common.hart.enums.StopBit;
import org.hy.common.hart.enums.TimeoutMode;
import org.hy.common.modbus.IModbus;
import org.hy.common.modbus.data.MConnConfig;
import org.hy.common.modbus.data.MDataItem;
import org.hy.common.modbus.enums.ModbusData;
import org.hy.common.modbus.enums.ModbusProtocol;
import org.hy.common.modbus.enums.ModbusType;
import org.hy.common.modbus.modbus4j.Modbus4J;
import org.hy.common.xml.annotation.Xjava;





/**
 * 测试单元：粘度测试仪 
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-06-25
 * @version     v1.0
 */
@Xjava
public class JU_Viscosity
{
    
    public void test_Modbus4j_RTU() throws InterruptedException
    {
        List<MDataItem> v_Datagram = new ArrayList<MDataItem>();
        v_Datagram.add(new MDataItem("V1" ,1 ,ModbusData.D2B_Int_Signed));
        v_Datagram.add(new MDataItem("V2" ,2 ,ModbusData.D2B_Int_Signed));
        
        MConnConfig v_Config = new MConnConfig();
        v_Config.setCommPortName("Physical Port S1");
        v_Config.setProtocol(ModbusProtocol.RTU);
        v_Config.setType(    ModbusType.Master);
        v_Config.setBaudRate(9600);
        v_Config.setDataBits(DataBit.DataBit_8);
        v_Config.setStopBit( StopBit.One);
        v_Config.setParityCheck(Parity.None);
        v_Config.setFrequency(100);
        v_Config.setTimeout(1000 * 10);
        v_Config.setReadTimeout(1000);
        v_Config.setWriteTimeout(1000);
        v_Config.setTimeoutModes(TimeoutMode.NonBlocking.getValue());
        v_Config.setFlowControls(FlowControl.Disabled.getValue());
        
        IModbus v_Modbus = new Modbus4J(v_Config);
        if  ( v_Modbus.init() )
        {
            System.out.println("初始化成功");
        }
        else
        {
            System.out.println("初始化失败");
            return;
        }
        
        for (int x=1; x<=10; x++)
        {
            System.out.println("准备读数据");
            Date                v_STime = new Date();
            Map<String ,Object> v_Nums  = v_Modbus.readOutputRegister(1 ,v_Datagram);
            Date                v_ETime = new Date();
            System.out.println(Date.toTimeLen(v_ETime.differ(v_STime)));
            System.out.println("完成读数据");
            
            Help.print(v_Nums);
            Thread.sleep(1000);
        }
    }
    
}
