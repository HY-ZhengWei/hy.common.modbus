package org.hy.common.modbus.junit.cflow026;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.hy.common.Date;
import org.hy.common.Return;
import org.hy.common.callflow.CallFlow;
import org.hy.common.callflow.execute.ExecuteResult;
import org.hy.common.callflow.forloop.ForConfig;
import org.hy.common.modbus.IModbus;
import org.hy.common.modbus.data.MConnConfig;
import org.hy.common.modbus.data.MDataItem;
import org.hy.common.modbus.enums.ModbusData;
import org.hy.common.modbus.enums.ModbusProtocol;
import org.hy.common.modbus.enums.ModbusType;
import org.hy.common.modbus.junit.JUBase;
import org.hy.common.modbus.junit.cflow026.program.Program;
import org.hy.common.modbus.modbus4j.Modbus4J;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;





/**
 * 测试单元：编排引擎026：Modbus写数据，温度算法
 *
 * @author      ZhengWei(HY)
 * @createDate  2025-07-14
 * @version     v1.0
 */
@Xjava(value=XType.XML)
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class JU_CFlow026 extends JUBase
{
    
    private static boolean $isInit = false;
    
    
    
    public JU_CFlow026() throws Exception
    {
        if ( !$isInit )
        {
            $isInit = true;
            XJava.parserAnnotation(this.getClass().getName());
            
            this.initDeviceXID();
            this.initDatagramXID();
        }
    }
    
    
    
    /**
     * 初始化物联设备
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-07-14
     * @version     v1.0
     *
     */
    private void initDeviceXID()
    {
        MConnConfig v_Config = new MConnConfig();
        v_Config.setHost("192.168.0.101");
        v_Config.setPort(502);
        v_Config.setProtocol(ModbusProtocol.TCP);
        v_Config.setType(    ModbusType.Master);
        
        IModbus v_Modbus = new Modbus4J(v_Config);
        v_Modbus.init();
        
        XJava.putObject("LED显示器_01" ,v_Modbus);
    }
    
    
    
    /**
     * 初始化数据报文
     * 
     * @author      ZhengWei(HY)
     * @createDate  2025-07-14
     * @version     v1.0
     *
     */
    private void initDatagramXID()
    {
        List<MDataItem> v_MItems1 = new ArrayList<MDataItem>();
        List<MDataItem> v_MItems2 = new ArrayList<MDataItem>();
        MDataItem       v_MItem   = null;
        
        // 第四行，第一列
        v_MItem = new MDataItem();
        v_MItem.setName("timed_04");
        v_MItem.setOffset(48);
        v_MItem.setDataType(ModbusData.DVarchar);
        v_MItems1.add(v_MItem);
        
        v_MItem = new MDataItem();
        v_MItem.setName("value_04");
        v_MItem.setOffset(56);
        v_MItem.setDataType(ModbusData.DVarchar);
        v_MItems1.add(v_MItem);
        
        
        // 第三行，第一列
        v_MItem = new MDataItem();
        v_MItem.setName("timed_03");
        v_MItem.setOffset(32);
        v_MItem.setDataType(ModbusData.DVarchar);
        v_MItems1.add(v_MItem);
        
        v_MItem = new MDataItem();
        v_MItem.setName("value_03");
        v_MItem.setOffset(40);
        v_MItem.setDataType(ModbusData.DVarchar);
        v_MItems1.add(v_MItem);
        
        
        // 第二行，第一列
        v_MItem = new MDataItem();
        v_MItem.setName("timed_02");
        v_MItem.setOffset(16);
        v_MItem.setDataType(ModbusData.DVarchar);
        v_MItems1.add(v_MItem);
        
        v_MItem = new MDataItem();
        v_MItem.setName("value_02");
        v_MItem.setOffset(24);
        v_MItem.setDataType(ModbusData.DVarchar);
        v_MItems1.add(v_MItem);
        
        
        // 第一行，第一列
        v_MItem = new MDataItem();
        v_MItem.setName("timed_01");
        v_MItem.setOffset(0);
        v_MItem.setDataType(ModbusData.DVarchar);
        v_MItems1.add(v_MItem);
        
        v_MItem = new MDataItem();
        v_MItem.setName("value_01");
        v_MItem.setOffset(8);
        v_MItem.setDataType(ModbusData.DVarchar);
        v_MItems1.add(v_MItem);
        v_MItems2.add(v_MItem);
        
        XJava.putObject("LEDData_01" ,v_MItems1);
        XJava.putObject("LEDData_02" ,v_MItems2);
    }
    
    
    
    @Test
    public void test_CFlow026() throws InterruptedException
    {
        test_CFlow026_Inner();
        
    }
    
    
    
    private void test_CFlow026_Inner() throws InterruptedException
    {
        // 初始化被编排的执行对象方法
        XJava.putObject("XProgram" ,new Program());
        
        // 获取编排中的首个元素
        ForConfig           v_ForConfig = (ForConfig) XJava.getObject("XFor_CF026_1");
        Map<String ,Object> v_Context   = new HashMap<String ,Object>();
        
        // 执行前的静态检查（关键属性未变时，check方法内部为快速检查）
        Return<Object> v_CheckRet = CallFlow.getHelpCheck().check(v_ForConfig);
        if ( !v_CheckRet.get() )
        {
            System.out.println(v_CheckRet.getParamStr());  // 打印不合格的原因
            return;
        }
        
        ExecuteResult v_Result = CallFlow.execute(v_ForConfig ,v_Context);
        if ( v_Result.isSuccess() )
        {
            System.out.println("Success");
        }
        else
        {
            System.out.println("Error XID = " + v_Result.getExecuteXID());
            System.out.println("Error Msg = " + v_Result.getException().getMessage());
            if ( v_Result.getException() instanceof TimeoutException )
            {
                System.out.println("is TimeoutException");
            }
            v_Result.getException().printStackTrace();
        }
        
        // 打印执行路径
        ExecuteResult v_FirstResult = CallFlow.getFirstResult(v_Context);
        System.out.println(CallFlow.getHelpLog().logs(v_FirstResult));
        System.out.println("整体用时：" + Date.toTimeLenNano(v_Result.getEndTime() - v_Result.getBeginTime()) + "\n");
        
        // 导出
        System.out.println(CallFlow.getHelpExport().export(v_ForConfig));
        
        toJson(v_ForConfig);
        toJson(v_FirstResult);
    }
    
}
