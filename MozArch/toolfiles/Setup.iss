; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=MozArch
AppVerName=MozArch version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\MozArch
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\MozArch.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\MozArch.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\MozArch"; Filename: "{app}\MozArch.exe"
