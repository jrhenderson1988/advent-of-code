package d23

import "container/heap"

type item struct {
	burrow     Burrow
	energyCost int
	index      int
}

type priorityQueue []*item

func (pq priorityQueue) Len() int {
	return len(pq)
}

func (pq priorityQueue) Less(i, j int) bool {
	return pq[i].energyCost < pq[j].energyCost
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

func (mpq *MinPriorityQueue) Add(burrow Burrow, energyCost int) {
	obj := &item{burrow: burrow, energyCost: energyCost, index: -1}
	heap.Push(&mpq.pq, obj)
}

func (mpq *MinPriorityQueue) Pop() Burrow {
	obj := heap.Pop(&mpq.pq).(*item)
	return obj.burrow
}
