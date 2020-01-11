# -*- coding: utf-8 -*-
"""
Este script calcula as médias de um arquivo de resultados e envia por email
@author Cesar
@name sendMail 1.0
"""

import pandas as pd
import numpy as np
import smtplib

from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText

print('Incializando dataframe ...')

df = pd.read_csv('gaps.csv', sep='\t', usecols=[2, 3, 4, 5, 6, 7, 8, 9])

algoritmo = 'SA'
lit = df['LIT.'].mean()
melhor = df['MELHOR'].mean()
pior = df['PIOR'].mean()
media = df['MEDIA'].mean()
mediana = df['MEDIANA'].mean()
gap_melhor = df['RPDbst'].mean()
gap_media = df['RPDavg'].mean()
tempo = df['TEMPO(s)'].mean()

df_resumo = pd.DataFrame(data=[[algoritmo, lit, melhor, pior, media, mediana, gap_melhor, gap_media, tempo]],
                        columns=['ALGORITMO','LITERATURA', 'MELHOR', 'PIOR', 'MEDIA', 'MEDIANA', 'GAP MELHOR', 'GAP MEDIA', 'TEMPO(s)'])

print('Gerando HTML ...')
texto = df_resumo.to_html()

html1 = '''<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta content="text/html; charset=ISO-8859-1" http-equiv="content-type">
<title>RESULTADOS</title>
<style>
.dataframe {
  font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
  border-collapse: collapse;
  width: 100%;
}

.dataframe td, .dataframe th {
  border: 1px solid #ddd;
  padding: 8px;
}

.dataframe tr:nth-child(even){background-color: #f2f2f2;}

.dataframe tr:hover {background-color: #ddd;}

.dataframe th {
  padding-top: 12px;
  padding-bottom: 12px;
  text-align: left;
  background-color: #4CAF50;
  color: white;
}
</style>
<title>RESULTADOS</title>
</head>
<body>
'''

html2 = '''
<br>
<br>
<br>
</body>
</html>'''

textofinal = html1 + texto + html2

# me == my email address
# you == recipient's email address
me = "cesar@cefetmg.br"
you = "cesar@cefetmg.br"

# Create message container - the correct MIME type is multipart/alternative.
msg = MIMEMultipart('alternative')
msg['Subject'] = "Resultados"
msg['From'] = me
msg['To'] = you

# Create the body of the message (a plain-text and an HTML version).
text = "Hi!\nHow are you?\nHere is the link you wanted:\nhttp://www.python.org"
html = """\
<html>
  <head></head>
  <body>
    <p>Hi!<br>
       How are you?<br>
       Here is the <a href="http://www.python.org">link</a> you wanted.
    </p>
  </body>
</html>
"""

# Record the MIME types of both parts - text/plain and text/html.
part1 = MIMEText(text, 'plain')
part2 = MIMEText(textofinal, 'html')

# Attach parts into message container.
# According to RFC 2046, the last part of a multipart message, in this case
# the HTML message, is best and preferred.
msg.attach(part1)
msg.attach(part2)
# Send the message via local SMTP server.
mail = smtplib.SMTP('smtp.cefetmg.br', 587)

mail.ehlo()
print('Enviando email...')
mail.starttls()

mail.login('cesar@cefetmg.br', 'Noisebao32')
mail.sendmail(me, you, msg.as_string())
mail.quit()
print('E-mail enviado!')
