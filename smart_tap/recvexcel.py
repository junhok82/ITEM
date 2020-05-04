#!/usr/bin/env python3
import socket
import sys
from time import sleep

TCP_IP='172.31.16.204'
TCP_PORT = 8000
BUFFER_SIZE = 1024

s=socket.socket(socket.AF_INET,socket.SOCK_STREAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
print('bind()')
s.bind((TCP_IP, TCP_PORT))
print('listen...')
s.listen(1)

conn, addr = s.accept()
print(addr)
print('connection address:', addr)

filename_xlsx = './rawdata_sheet.xlsx'
with open(filename_xlsx, 'wb') as f:
	print('file opened')
	str_done = ''
	cnt = 0
	while True:
		print(cnt, end=' ')
		cnt += 1
		data = conn.recv(1024)
		print(sys.getsizeof(data))
		if not data:
			print('while break - if not data')
			break
		try:
			if sys.getsizeof(data)<1024:
				str_done = data.decode('utf-8').strip()
		except:
			pass
		else:
			if str_done == 'done sending':
				print('while break - if str_done=="done sending"')
				break
		f.write(data)
		f.flush()
	print('Done sending')
	f.close()

try:
	data = conn.recv(BUFFER_SIZE)
	str_done = data.decode('utf-8').strip()
except:
	print('final decode err')
	pass
else:
	print('str_done :', str_done)

conn.close()
