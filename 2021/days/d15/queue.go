package d15

import "container/heap"

type item struct {
	point coord
	dist  int
	index int
}

type priorityQueue []*item

func (pq priorityQueue) Len() int {
	return len(pq)
}

func (pq priorityQueue) Less(i, j int) bool {
	return pq[i].dist < pq[j].dist
}

func (pq *priorityQueue) Pop() interface{} {
	old := *pq
	n := len(old)
	obj := old[n-1]
	obj.index = -1
	*pq = old[:n-1]
	return obj
}

func (pq *priorityQueue) Push(x interface{}) {
	n := len(*pq)
	obj := x.(*item)
	obj.index = n
	*pq = append(*pq, obj)
}

func (pq priorityQueue) Swap(i, j int) {
	pq[i], pq[j] = pq[j], pq[i]
	pq[i].index = i
	pq[j].index = j
}

type MinPriorityQueue struct {
	pq priorityQueue
}

func NewMinPriorityQueue() *MinPriorityQueue {
	pq := make(priorityQueue, 0)
	heap.Init(&pq)
	return &MinPriorityQueue{pq}
}

func (mpq *MinPriorityQueue) Len() int {
	return mpq.pq.Len()
}

func (mpq *MinPriorityQueue) Add(point coord, dist int) {
	obj := &item{point: point, dist: dist, index: -1}
	heap.Push(&mpq.pq, obj)
}

func (mpq *MinPriorityQueue) Pop() coord {
	obj := heap.Pop(&mpq.pq).(*item)
	return obj.point
}
