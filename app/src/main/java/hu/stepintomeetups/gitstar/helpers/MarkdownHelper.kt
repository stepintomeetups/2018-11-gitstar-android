/*
 * Created by Tamás Szincsák on 2018-11-04.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.helpers

fun CharSequence.getFirstNLines(n: Int): CharSequence {
    var last = -1
    for (i in 1..n) {
        val occurrence = indexOf('\n', last + 1)
        if (occurrence == -1) {
            last = -1
            break
        }

        last = occurrence
    }

    return if (last > -1) subSequence(0, last) else this
}
