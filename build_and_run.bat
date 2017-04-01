:beginning
javac -cp "./metadata-extractor-2.9.1.jar;./xmpcore-5.1.2.jar" DescribeIt.java 
@IF NOT %ERRORLEVEL%==0 (pause 
goto end )

java DescribeIt -cp "./xmpcore-5.1.2.jar;./metadata-extractor-2.9.1.jar"

@IF NOT %ERRORLEVEL%==0 pause
echo.
echo.
echo Build and run again?
pause
:end
goto beginning