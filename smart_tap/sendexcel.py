import socket
import sys
from time import sleep

TCP_IP = '15.164.14.98'
#TCP_IP = '127.0.0.1'
TCP_PORT = 8000
BUFFER_SIZE = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('connect...')
s.connect((TCP_IP, TCP_PORT))


filename = 'rawdata_sheet.xlsx'
f = open(filename, 'rb')
l = f.read(1024)
while(l):
	s.send(l)
	sleep(0.01)
	l = f.read(1024)
sleep(0.5)
s.send(('done sending').encode('utf-8'))
print('xlsx sending done')
f.close()

s.close()
