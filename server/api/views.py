# -*- coding: utf-8 -*-
from django.shortcuts import render
from django.http import HttpResponse

import random
import string
import os
import sys
import json
sys.path.insert(0, '/home/viperk/MinimalBOW')
#import package here
import classify

# Create your views here.
def home(request):
	return HttpResponse("hello")

def add(request):
	if request.method == 'GET':
		a = int(request.GET['a'])
		b = int(request.GET['b'])
		return HttpResponse(a+b)
	
	if request.method == 'POST':
		return HttpResponse(123)

	return HttpResponse('?')

def predict(request):
	if request.method == 'POST':
		s = request.POST['img']
		filename = create_received_file(s)
		os.chdir('/home/viperk/MinimalBOW/')
		'''os.system('python classify.py -c traincodebook.file -m traintrainingdata.svm.model image.jpeg')
		print('uweqyweqyweq')'''
		label = classify.test_func('/home/viperk/server/api/'+filename)
		f = json.load(open('/home/viperk/MinimalBOW/label.txt'))
		return HttpResponse(f[str(label[0])])
	return  HttpResponse('predict place label')

def create_received_file(filecontent):
	tmp_filename = ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(64)) + '.jpg'
	#handle_uploaded_file(request.POST['quyen'], tmp_filename) #random name
	img = filecontent.decode('base64')
	f = open('/home/viperk/server/api/'+tmp_filename, 'wb')
	f.write(img)
	f.close()
	return tmp_filename
	#model.process('media/%s'%(tmp_filename))
	#media/123.jpg
	# ......
	# res
