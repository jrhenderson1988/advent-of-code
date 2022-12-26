defmodule AoC.Common.PriorityQueue do
  defstruct [:set]

  def new(), do: %__MODULE__{set: :gb_sets.empty()}
  def new([]), do: new()

  def add_with_priority(%__MODULE__{} = q, elem, prio) do
    %{q | set: :gb_sets.add({prio, elem}, q.set)}
  end

  def size(%__MODULE__{} = q) do
    :gb_sets.size(q.set)
  end

  def extract_min(%__MODULE__{} = q) do
    case :gb_sets.size(q.set) do
      0 ->
        :empty

      _ ->
        {{prio, elem}, set} = :gb_sets.take_smallest(q.set)
        {{prio, elem}, %{q | set: set}}
    end
  end
end
