import subprocess

tests_run = 0
tests_passed = 0

def run_test(command, output):
	global tests_run
	global tests_passed
	
	# run executable
	result = subprocess.check_output(command, shell=True).decode('ascii')
	
	# yeet newlines
	output = output.rstrip()
	result = result.rstrip()
	
	# check result
	if output == result:
		tests_passed += 1
	else:
		print("Command:  ", command)
		print("Expected: ", output)
		print("Received: ", result)
		print("")
	tests_run += 1


# Testing: every possible output, USD

run_test("resp 0$",			"Project Manager")
run_test("resp 499$",		"Project Manager")

run_test("resp 500$",		"Program Manager")
run_test("resp 501$",		"Program Manager")
run_test("resp 1999$",		"Program Manager")

run_test("resp 2000$",		"Subdivision Manager")
run_test("resp 2001$",		"Subdivision Manager")
run_test("resp 4999$",		"Subdivision Manager")

run_test("resp 5000$",		"Division Director")
run_test("resp 5001$",		"Division Director")
run_test("resp 19999$",		"Division Director")

run_test("resp 20000$",		"CEO")
run_test("resp 20001$",		"CEO")
run_test("resp 99999$",		"CEO")

run_test("resp 100000$",	"Not approved.")
run_test("resp 100001$",	"Not approved.")
run_test("resp 999999$",	"Not approved.")


# Testing: every possible output, CAD

run_test("resp -default CAD 0CAD",		"Project Manager")
run_test("resp 499CAD -default CAD",	"Project Manager")

run_test("resp -default CAD 500CAD",	"Program Manager")
run_test("resp 501CAD -default CAD",	"Program Manager")
run_test("resp -default CAD 1999CAD",	"Program Manager")

run_test("resp 2000CAD -default CAD",	"Subdivision Manager")
run_test("resp -default CAD 2001CAD",	"Subdivision Manager")
run_test("resp 4999CAD -default CAD",	"Subdivision Manager")

run_test("resp -default CAD 5000CAD",	"Division Director")
run_test("resp 5001CAD -default CAD",	"Division Director")
run_test("resp -default CAD 19999CAD",	"Division Director")

run_test("resp 20000CAD -default CAD",	"CEO")
run_test("resp -default CAD 20001CAD",	"CEO")
run_test("resp 99999CAD -default CAD",	"CEO")

run_test("resp -default CAD 100000CAD",	"Not approved.")
run_test("resp 100001CAD -default CAD",	"Not approved.")
run_test("resp -default CAD 999999CAD",	"Not approved.")


print("Tests run:\t", tests_run)
print("Tests passed:\t", tests_passed)