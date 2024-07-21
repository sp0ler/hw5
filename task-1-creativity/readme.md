# Задание 1. Творческое

Придумайте и напишите (словами) пример использования (в каких случаях вы бы его использовали) каждого из стандартных пулов потоков:

* FixedThreadPool;
* CachedThreadPool;
* SingleThreadExecutor;
* ScheduledExecutorService;
* ForkJoinPool.

## FixedThreadPool

```text
# Напишите ниже свои мыли и идеи
> В EventLister при работе с кафкой
```

## CachedThreadPool

```text
# Напишите ниже свои мыли и идеи
> В парадигме MVC при асинхронных запросах к другим сервисам. 
Но при условии что есть Circuit Breaker, иначе можно словить OOM.
Если нет, то лучше FixedThreadPool бы для это задачи применил.
```

## SingleThreadExecutor

```text
# Напишите ниже свои мыли и идеи
> Для каких-то задач, которые выполняется последовательно. 
Сделал бы какую-то очередь, куда все потоки складвают данные, 
а SingleThreadExecutor что-то с ними делал.
```

## ScheduledExecutorService

```text
# Напишите ниже свои мыли и идеи
> В задачах с отложенным выполнением, на подобии Spring Scheduler.
```

## ForkJoinPool

```text
# Напишите ниже свои мыли и идеи
> Для разделения одной задачи на подзадачи. 
Например все запросы в Spring Framework обрабатываются на ForkJoinPool.
```

