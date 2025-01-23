

del /Q hy.common.modbus.jar
del /Q hy.common.modbus-sources.jar


call mvn clean package
cd .\target\classes

rd /s/q .\org\hy\common\modbus\junit


jar cvfm hy.common.modbus.jar META-INF/MANIFEST.MF META-INF org

copy hy.common.modbus.jar ..\..
del /q hy.common.modbus.jar
cd ..\..





cd .\src\main\java
xcopy /S ..\resources\* .
jar cvfm hy.common.modbus-sources.jar META-INF\MANIFEST.MF META-INF org 
copy hy.common.modbus-sources.jar ..\..\..
del /Q hy.common.modbus-sources.jar
rd /s/q META-INF
cd ..\..\..

pause