        lw      1,0,five    # load reg1 with 5

        lw      2,1,2

start   add     1,1,2

        beq     0,1,done

        j       start

done    halt

five    .fill   5

neg1    .fill   -1

stAddr  .fill   start       # will contain
