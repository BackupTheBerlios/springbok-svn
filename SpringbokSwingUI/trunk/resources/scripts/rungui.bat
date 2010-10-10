SET JAVA_HOME=jre6
SET PATH=jre6\bin;%PATH%
for %%i in ("libs\*.jar") do call addtoclasspath.bat %%i
#java -Dswing.defaultlaf=com.birosoft.liquid.LiquidLookAndFeel -jar SpringbokUI.jar
java -Dswing.defaultlaf=com.birosoft.liquid.LiquidLookAndFeel ui.SpringbokUI
