(def map-defaults (fn [default keys]
                    (letfn [(rec [default keys result]
                              (if (empty? keys)
                                result
                                (rec default (rest keys) (cons (first keys) (cons default result)))))]
                      (apply hash-map (rec default keys '())))))

(def comparisons (fn [comp x y]
                   (let [a (apply comp (list x y))
                         b (apply comp (list y x))]
                     (cond (and (not a) (not b)) :eq
                            a :lt
                            b :gt))))

(def count-seq (fn [seq]
                 (letfn [(rec [seq n]
                           (if (empty? seq)
                             n
                             (rec (rest seq) (inc n))))]
                   (rec seq 0))))

(def my-interpose (fn [default lst]
                 (letfn [(rec [default lst result]
                           (if (empty? lst)
                             result
                             (rec default (butlast lst) (cons (last lst) (cons default result)))))]
                   (butlast (rec default lst '())))))

(def fibonacci (fn fib [n]
                 (cond (= n 1) '(1)
                       (= n 2) '(1 1)
                       true (concat (fib (- n 1)) (list (+ (nth (fib (dec n)) (- n 3)) (nth (fib (dec n)) (- n 2))))))))

(def my-reverse (fn my-rev [lst]
                  (if (empty? lst)
                    '()
                    (cons (last lst) (my-rev (butlast lst))))))

(defn infix-calc [& lst]
  (cond (= (count lst) 1) (nth lst 0)
        (= (count lst) 3) (apply (nth lst 1) (list (nth lst 0) (nth lst 2)))
        true (apply infix-calc (apply infix-calc (the-first lst 3)) (nthrest lst 3))))

(defn the-first [lst n]
  (reverse (nthrest (reverse lst) (- (count lst) n))))

(def infix-calculator (fn infix-calc [& lst]
                        (letfn [(the-first [lst n]
                                  (reverse (nthrest (reverse lst) (- (count lst) n))))]
                          (cond (= (count lst) 1) (nth lst 0)
                                (= (count lst) 3) (apply (nth lst 1) (list (nth lst 0) (nth lst 2)))
                                true (apply infix-calc (apply infix-calc (the-first lst 3)) (nthrest lst 3))))))


(def primes-list (fn [n] (letfn [(next-prime [n]
                  (letfn [(is-prime [x]
                            (not (= 0 (some #{0} (for [i (range 2 x)]
                                              (mod x i))))))]
                    (let [next-int (inc n)]
                      (if (is-prime next-int) next-int (next-prime next-int)))))]
                           (loop [i 0
                                  result [2]]
                             (if (> i (- n 2)) result
                                 (recur (inc i) (conj result (next-prime (last result)))))))))

(def sum-square-of-digits (fn [lst]
                            (letfn [(num2digits [n]
                                      (map #(read-string (str %)) (str n)))
                                    (sum-of-digits [n]
                                      (apply + (map #(* % %) (num2digits n))))]
                              (count (filter #(= % true) (map < lst (map sum-of-digits lst)))))))

(def replicate-sequence (fn [lst n]
                          (letfn [(splice [src items]
                                    (if (= 0 (count items))
                                      src
                                      (splice (conj src (first items)) (rest items))))]
                            (loop [i 0 result '()]
                              (if (> i (dec (count lst)))
                                (reverse result)
                                (recur (inc i) (splice result (repeat n (nth lst i)))))))))

(def replicate-seq-shorter (fn [lst n]
                             (loop [i 0 result '()]
                               (if (> i (dec (count lst)))
                                 result
                                 (recur (inc i) (concat result (repeat n (nth lst i))))))))

;;; not lazy
(def my-iterate (fn [f x]
                  (loop [i 0 result (list x)]
                    (if (> i 101)
                      result
                      (recur (inc i) (concat result (list (f (last result)))))))))

(defn splice [src items]
  (if (= 0 (count items))
    src
    (splice (conj src (first items)) (rest items))))

(def recog-card (fn [card]
                   (let [suit {"C" :club "D" :diamond "H" :heart "S" :spade}
                         rank {"2" 0 "3" 1 "4" 2 "5" 3 "6" 4 "7" 5 "8" 6 "9" 7 "T" 8 "J" 9 "Q" 10 "K" 11 "A" 12}
                         card-suit (subs card 0 1)
                         card-rank (subs card 1 2)]
                     (hash-map :suit (suit card-suit) :rank (rank card-rank)))))

;;; broken
(def infix-calc (fn infcalc [& args]
                  (if (= (count args) 3)
                    (apply (nth args 1) (list (nth args 0) (nth args 2)))
                    (apply (infcalc (flatten (list (infcalc (reverse (nthrest (reverse args) 2))) (nthrest args 3))))))))

;;; broken
(defn infcalc [& args]
  (if (= (count args) 3)
    (apply (nth args 1) (list (nth args 0) (nth args 2)))
    (apply infcalc (into [] (flatten (list (infcalc (reverse (nthrest (reverse args) (- (count args) 3)))) (nthrest args 3)))))))

(defn compress [lst]
  (letfn [(rec [lst acc]
            (if (empty? lst)
              acc
              (rec (rest lst) (if (not (= (first acc) (first lst))) (cons (first lst) acc) acc))))]
    (reverse (rec lst '()))))

(defn num2digits [n]
  (map read-string (map str (seq (str n)))))

(defn product-digits [a b]
  (letfn [(num2digits [n]
            (map read-string (map str (seq (str n)))))]
    (into [] (num2digits (* a b)))))

(defn my-range [a b]
  (take (- b a) (iterate #(+ 1 %) a)))

(defn my-subseq [lst start end]
  (let [remove-from-end (- (count lst) end)
        reversed (reverse lst)
        reversed-minus-tail (nthrest reversed remove-from-end)]
    (nthrest (reverse reversed-minus-tail) start)))

(defn my-part2 [n source]
  (letfn [(my-partition [n source]
            (letfn [(mysubseq [lst start end]
            (let [remove-from-end (- (count lst) end)
                  reversed (reverse lst)
                  reversed-minus-tail (nthrest reversed remove-from-end)]
              (nthrest (reverse reversed-minus-tail) start)))
          
          (rec [source acc]
            (let [rest (nthrest source n)]
              (if (not (empty? rest))
                (rec rest (cons (mysubseq source 0 n) acc))
                (reverse (cons source acc)))))]
    (if (not (empty? source)) (rec source nil) nil)))]
    (filter #(= n (count %)) (my-partition n source))))

(defn my-subseq2 [lst a b]
  (take (- b a) (drop a lst)))

(defn my-dupl [lst]
  (letfn [(rec [lst acc]
            (if (empty? lst)
              acc
              (rec (rest lst) (cons (first lst) (cons (first lst) acc)))))]
    (reverse (rec lst '()))))

;;; juxt  subvec

(defn drop-nth [lst n]
  (loop [i 1
         x lst
         result []]
    (if (empty? x)
      result
      (recur (inc i) (rest x)
             (if (not (= (mod i n) 0))
               (conj result (first x))
               result)))))

(def my-interleave (fn [lsta lstb]
  (letfn [(interl-iter [a b result]
            (if (or (empty? a) (empty? b)) result
                (interl-iter (rest a) (rest b) (cons (first b) (cons (first a) result)))))]
    (reverse (interl-iter lsta lstb '())))))

(def indexing-seq (fn [lst]
                    (letfn [(make-pair [a b] (cons a (cons b '())))]
                      (loop [i (dec (count lst)) rem lst result '()]
                        (if (empty? rem)
                          result
                          (recur (dec i) (butlast rem) (cons (make-pair (last rem) i) result)))))))

(def find-distinct (fn [lst]
                     (letfn [(find-dist-iter [input seen output]
                               (if (empty? input)
                                 output
                                 (find-dist-iter (rest input)
                                                 (cons (first input) seen)
                                                 (if (some #(= (first input) %) seen)
                                                   output
                                                   (conj output (first input))))))]
                       (find-dist-iter lst [] []))))

(defn get-divisors [n]
  (if (= n 1)
    [1]
    ;; first divisor plus get-divisors of (/ n first-divisor)
    (letfn [(test-div [i n]
              (= (mod n i) 0))]
      (loop [i 2]
        (if (test-div i n)
          (cons i (get-divisors (/ n i)))
          (recur (inc i)))))))

(def perfect-nums (fn [n]
                    (loop [i 1 divs []]
                      (if (> i (dec n))
                        (= n (reduce + divs))
                        (recur (inc i) (if (= (mod n i) 0)
                                       (conj divs i)
                                       divs))))))

(def my-juxt (fn
               ([f]
                  (fn
                    ([] [(f)])
                    ([x] [(f x)])
                    ([x y] [(f x y)])))
               ([f g]
                  (fn
                    ([] [(f) (g)])
                    ([x] [(f x) (g x)])))
               ([f g h]
                  (fn
                    ([a b c d e q] [(f a b c d e q) (g a b c d e q) (h a b c d e q)])
                    ([a b c] [(f a b c) (g a b c) (h a b c)])))))

(def map-constr (fn map-constr-rec [k v]
                  (if (or (empty? k) (empty? v))
                    (hash-map)
                    (assoc (map-constr-rec (rest k) (rest v)) (first k) (first v)))))

(def my-set-intersect (fn [m n]
                        (letfn [(build-inters [m n res]
                                  (if (empty? m)
                                    res
                                    (build-inters (rest m) n (if (some #(= (first m) %) n) (conj res (first m)) res))))]
                          (build-inters m n #{}))))

(def is-tree (fn [x]
               (letfn [(single? [n] (not (coll? n)))]
                 (if (= (count x) 3)
                   (if 
                       (and (single? (first x))
                            (or (single? (nth x 1)) (is-tree (nth x 1)))
                            (or (single? (nth x 2)) (is-tree (nth x 2))))
                     true
                     false)
                   false))))

(def pascal-tri (fn [n]
                  (cond (= n 1) [1]
                        (= n 2) [1 1]
                        true (let [prev-row (pascal-tri (dec n))]
                               (flatten (conj [1] (map + (butlast prev-row)
                                                       (rest prev-row)) [1]))))))

(defn pos-int
  ([] (pos-int 1))
  ([n] (cons n (lazy-seq (pos-int (inc n))))))

(def my-map (fn mmap [f s]
              (if (empty? s) '()
                  (cons (f (first s)) (lazy-seq (mmap f (rest s)))))))

;;; 6artek's solution
(def pascal-tri6 (fn pt [n]
                   (if (= n 1)
                     [1]
                     (let [prev (pt (dec n))]
                       (vec
                        (map + (cons 0 prev) (conj prev 0)))))))

(defn trap-one [n]
  (map + (cons 0 n) (conj n 0)))


;;; need +' for arbitrary length integers
(def pascal-trap (fn pt [n]
                   (let [next (map +' (cons 0 n) (conj (into [] n) 0))]
                     (cons n (lazy-seq (pt next))))))