; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=Tegels
AppVerName=Tegels version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\Tegels
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\Tegels.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\Tegels.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\Tegels"; Filename: "{app}\Tegels.exe"
