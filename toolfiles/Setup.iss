; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=WiskOpdr
AppVerName=WiskOpdr version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\WiskOpdr
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\WiskOpdr.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\WiskOpdr.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\WiskOpdr"; Filename: "{app}\WiskOpdr.exe"
