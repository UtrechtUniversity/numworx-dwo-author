; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=Spot_Problems_dwo
AppVerName=Spot_Problems_dwo version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\Spot_Problems_dwo
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\Spot_Problems_dwo.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\Spot_Problems_dwo.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\Spot_Problems_dwo"; Filename: "{app}\Spot_Problems_dwo.exe"
