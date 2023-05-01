; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=CalculatorDwo
AppVerName=CalculatorDwo version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\CalculatorDwo
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\CalculatorDwo.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\CalculatorDwo.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\CalculatorDwo"; Filename: "{app}\CalculatorDwo.exe"
