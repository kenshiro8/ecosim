' launch.vbs
Dim WshShell, fso, scriptDir
Set WshShell = CreateObject("WScript.Shell")
Set fso      = CreateObject("Scripting.FileSystemObject")

' この .vbs ファイルが置かれたフォルダを取得
scriptDir = fso.GetParentFolderName(WScript.ScriptFullName)

' run-jar.bat を隠しモード(0)で実行。最後の False は同期実行オプション
WshShell.Run """" & scriptDir & "\run-jar.bat""", 0, False

Set WshShell = Nothing
Set fso      = Nothing