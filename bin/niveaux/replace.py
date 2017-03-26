 # Read in the file
filedata = None
file = open('lvl102.sok', 'r')
filedata = file.read()
print(filedata)

# Replace the target string
filedata.replace('x', '#')
filedata.replace('t', '.')
filedata.replace('c', '$')


# Write the file out again
file = open('file.txt', 'w')
file.write(filedata)
