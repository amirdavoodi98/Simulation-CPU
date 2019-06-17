        addi    1,1,20
		
	    addi    2,2,10

		lw      1,1,2    # load reg1 with 5

        lw      2,1,2

start   add     1,1,2

        beq     0,1,done


	    addi    2,2,10

	    addi    4,2,20
done    halt

five    .fill   5

neg1    .fill   -1

stAddr  .fill   start       # will contain


