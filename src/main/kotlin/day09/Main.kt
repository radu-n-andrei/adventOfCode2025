package org.example.day09

import org.example.LoadInput
import kotlin.collections.plus
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


data class Tile(val x: Int, val y: Int) {

    companion object {
        fun parse(str: String): Tile {
            val parts = str.split(",").map(String::toInt)
            return Tile(parts[0], parts[1])
        }
    }

    fun area(other: Tile): Long {
        return (abs(x - other.x) + 1L) * (abs(y - other.y) + 1L)
    }

    fun outline(oppositeCorner: Tile): List<Tile> {
        val horizontal = if (oppositeCorner.x < x) (oppositeCorner.x..x).flatMap { cx ->
            listOf(
                Tile(cx, y),
                Tile(cx, oppositeCorner.y)
            )
        }
        else (x..oppositeCorner.x).flatMap { cx ->
            listOf(
                Tile(cx, y),
                Tile(cx, oppositeCorner.y)
            )
        }
        val vertical = if (oppositeCorner.y < y) (oppositeCorner.y..y).flatMap { cy ->
            listOf(
                Tile(x, cy),
                Tile(oppositeCorner.x, cy)
            )
        }
        else (y..oppositeCorner.y).flatMap { cy ->
            listOf(
                Tile(x, cy),
                Tile(oppositeCorner.x, cy)
            )
        }
        return (horizontal + vertical).distinct()
    }

    fun projectIntoTheBeyond(routes: List<Route>): Boolean {
        if (routes.find { r -> r.isOnRoute(this) } != null)
            return true
        else {
            val projectionTowardsN =
                routes.filter { r ->
                    !onSamePlane(r.routeDirection, Direction.N) && r.start.y < y && r.isOnRoute(
                        Tile(
                            x,
                            r.start.y
                        )
                    )
                }.minByOrNull { r -> y - r.start.y }
            val projectionTowardsS =
                routes.filter { r ->
                    !onSamePlane(r.routeDirection, Direction.S) && r.start.y > y && r.isOnRoute(
                        Tile(
                            x,
                            r.start.y
                        )
                    )
                }.minByOrNull { r -> r.start.y - y }
            val projectionTowardsE =
                routes.filter { r ->
                    !onSamePlane(r.routeDirection, Direction.E) && r.start.x > x && r.isOnRoute(
                        Tile(
                            r.start.x,
                            y
                        )
                    )
                }.minByOrNull { r -> r.start.x - x }
            val projectionTowardsW =
                routes.filter { r ->
                    !onSamePlane(r.routeDirection, Direction.W) && r.start.x < x && r.isOnRoute(
                        Tile(
                            r.start.x,
                            y
                        )
                    )
                }.minByOrNull { r -> x - r.start.x }
            val projections = listOf(
                projectionTowardsN?.projectionTraveling(Direction.N),
                projectionTowardsS?.projectionTraveling(Direction.S),
                projectionTowardsE?.projectionTraveling(Direction.E),
                projectionTowardsW?.projectionTraveling(Direction.W)
            )
            val result = projections.indexOf(null) < 0 &&
                    projections.map { p -> p ?: Side.L }.distinct().size == 1
            return result
        }
    }
}

enum class Direction {
    N, S, E, W
}

fun onSamePlane(dir1: Direction, dir2: Direction): Boolean {
    return dir1 == dir2 ||
            (dir1 == Direction.N && dir2 == Direction.S) ||
            (dir1 == Direction.S && dir2 == Direction.N) ||
            (dir1 == Direction.E && dir2 == Direction.W) ||
            (dir1 == Direction.W && dir2 == Direction.E)
}


enum class Side {
    L, R
}

data class Route(val start: Tile, val end: Tile) {

    val routeDirection: Direction =
        if (start.x == end.x) {
            if (start.y < end.y) Direction.S
            else Direction.N
        } else {
            if (start.x < end.x) Direction.E else Direction.W
        }

    fun isOnRoute(tile: Tile): Boolean {
        return (start.x == end.x && tile.x == start.x
                && tile.y <= max(start.y, end.y)
                && tile.y >= min(start.y, end.y)) ||
                (start.y == end.y && tile.y == end.y
                        && tile.x <= max(start.x, end.x)
                        && tile.x >= min(start.x, end.x))
    }

    fun projectionTraveling(towards: Direction): Side {
        return when (towards) {
            Direction.E -> if (routeDirection == Direction.N) Side.L else Side.R
            Direction.W -> if (routeDirection == Direction.N) Side.R else Side.L
            Direction.S -> if (routeDirection == Direction.E) Side.L else Side.R
            Direction.N -> if (routeDirection == Direction.E) Side.R else Side.L
        }
    }
}

var knownOut: List<Tile> = emptyList()

fun main() {
    val input = LoadInput.load("/day09/input.in") { br -> br.readLines().map(Tile::parse) }
    val sortedInput = input.sortedWith { o1, o2 ->
        if (o1.y == o2.y) o1.x - o2.x else o1.y - o2.y
    }
    println("P1:" + sortedInput.dropLast(1).fold(Pair(0L, sortedInput.drop(1))) { acc, tile ->
        val (maxArea, remainingTiles) = acc
        val m = remainingTiles.maxOf { t -> tile.area(t) }
        Pair(
            if (maxArea > m) maxArea else m,
            remainingTiles.drop(1)
        )
    }.first)
    val routes = input.zipWithNext().map { (a, b) -> Route(a, b) }.plus(Route(input.last(), input.first()))

    println("P2:" + sortedInput.dropLast(1).fold(Pair(0L, sortedInput.drop(1))) { acc, tile ->
        val (maxArea, remainingTiles) = acc
        // could use some memos here
        val maxxing = remainingTiles.fold(maxArea) { max, t ->
            val simpleArea = tile.area(t)
            if (simpleArea < max) {
                max
            } else {
                if (checkRectangleMini(tile, t, routes)) {
                    simpleArea
                } else max
            }
        }
        Pair(
            maxxing,
            remainingTiles.drop(1)
        )
    }.first)
}

fun checkRectangleMini(
    tile1: Tile,
    tile2: Tile,
    routes: List<Route>
): Boolean {
    val outline = tile1.outline(tile2)//.filter { t -> routes.find { r -> r.isOnRoute(t) } == null }
    return if (outline.intersect(knownOut).isNotEmpty()) false
    else {
        val outs = outline.find { t -> !t.projectIntoTheBeyond(routes) }
        if (outs == null) true
        else {
            knownOut += outs
            false
        }
    }
}