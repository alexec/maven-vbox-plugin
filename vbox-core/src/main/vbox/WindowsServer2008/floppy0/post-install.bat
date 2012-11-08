cmd /c certutil -addstore -f "TrustedPublisher" a:oracle-cert.cer
if %errorlevel% neq 0 exit %errorlevel%
d:\VBoxWindowsAdditions-x86.exe /S
if %errorlevel% neq 0 exit %errorlevel%
shutdown /s /t 0