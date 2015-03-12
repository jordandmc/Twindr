@echo off

java -jar JsTestDriver-1.3.5.jar --tests all --config SpecRunner.jstd --server http://127.0.0.1:9876
if errorlevel 1 (
  echo.
  echo.
  echo No browser open
  echo Run StartJSTestDriver.bat
  echo.
  echo Open a browser of your choice
  echo Navigate to http://localhost:9876
  echo Click Capture This Browser 
  echo Re-run this file
  echo.
  echo.
)

pause