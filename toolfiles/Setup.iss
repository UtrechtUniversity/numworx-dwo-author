; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=TekenVeelvlakOpdr
AppVerName=TekenVeelvlakOpdr version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\TekenVeelvlakOpdr
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\TekenVeelvlakOpdr.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\TekenVeelvlakOpdr.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\TekenVeelvlakOpdr"; Filename: "{app}\TekenVeelvlakOpdr.exe"
