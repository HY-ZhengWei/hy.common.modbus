#!/bin/sh

cd ./bin


rm -R ./org/hy/common/modbus/junit


jar cvfm hy.common.modbus.jar MANIFEST.MF META-INF org com

cp hy.common.modbus.jar ..
rm hy.common.modbus.jar
cd ..





cd ./src
jar cvfm hy.common.modbus-sources.jar MANIFEST.MF META-INF org com
cp hy.common.modbus-sources.jar ..
rm hy.common.modbus-sources.jar
cd ..
