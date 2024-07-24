import fileinput
import re

def verifyInvariants(file):
	line = [file.readline() , 0]
	while(True):
		
		line = re.subn('((T1(?!0))(.*?)(((T2)(.*?)(T4))|(T3)(.*?)(T5))(.*?)(T6))|(T7)(.*?)(T8)(.*?)(T9)(.*?)(T10)',
		'\g<3>\g<7>\g<10>\g<12>\g<15>\g<17>\g<19>', line[0].rstrip())
		
		if(line[1] == 0):
			break
		
	line = re.subn('-' , '' , line[0].rstrip())
	if(line[0] == ""):
		print("El test ha finalizado exitosamente.")
	else:
		print("Error de T-Invariantes: ")
		print(line[0])


def modifyLog():
    with open('output.txt', 'r') as archivo_entrada:
        # Lee todas las líneas del archivo y elimina los saltos de línea
        palabras = [palabra.strip() for palabra in archivo_entrada.readlines()]
    with open('resultado.txt', 'w') as archivo_salida:
        # Escribe todas las palabras en una sola línea
        archivo_salida.write(''.join(palabras))
    print("Proceso completado. Verifica el archivo 'resultado.txt'.")

modifyLog()
file = open("resultado.txt" , "r")
verifyInvariants(file)

