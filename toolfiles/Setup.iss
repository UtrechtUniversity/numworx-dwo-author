; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=NormaleVerdeling
AppVerName=NormaleVerdeling version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\NormaleVerdeling
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\NormaleVerdeling.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\NormaleVerdeling.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\NormaleVerdeling"; Filename: "{app}\NormaleVerdeling.exe"
