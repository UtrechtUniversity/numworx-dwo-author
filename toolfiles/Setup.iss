; -- Sample1.iss --
; Demonstrates copying 3 files and creating an icon.

; SEE THE DOCUMENTATION FOR DETAILS ON CREATING .ISS SCRIPT FILES!

[Setup]
AppName=GeomAlgebra
AppVerName=GeomAlgebra version 1.0
AppCopyright=Copyright (C) Freudenthal Instituut.
DefaultDirName={pf}\Wisweb\GeomAlgebra
DefaultGroupName=Wisweb
UninstallDisplayIcon={app}\GeomAlgebra.exe
MessagesFile=compiler:Dutch-1-2_0_18.isl
OutputDir=..\output\setup

[Files]
Source: "..\output\exe\GeomAlgebra.exe"; DestDir: "{app}"


[Icons]
Name: "{group}\GeomAlgebra"; Filename: "{app}\GeomAlgebra.exe"
