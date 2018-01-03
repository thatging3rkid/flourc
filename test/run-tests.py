#!/usr/bin/python3
"""
flourc testing script

:author: Connor Henley, @thatging3rkid
"""
import os

FLOURC_PATH = "bin/flourc"

# start by getting a list of all the directories (and check if it's a test)
for s in os.listdir():
    # check if it's a test
    if s.startswith("test-"):
        test_num = s[5:]  # store the test number as a string
        os.path.join(s)  # go into the dir
        p1 = subprocess.Popen(FLOURC_PATH + " prog." + test_num + ".flour", shell=True)
        p1.wait()  # wait for the compilation to finish

        p2 = subprocess.Popen("./a.out > got." + test_num, shell=True)
        p2.wait()  # run the program and pipe stdout to got.$testnum

        p3 = subprocess.Popen("diff expec." + test_num + " got." + s[5:], shell=True)
        if p3.wait() != 0:  # wait for the program to finish and get the return code
            print("test failed: `" + test_num + "`.", file=sys.stderr)  # in the future, the program should print the diff

        os.path.join("..")  # go back up
    pass

