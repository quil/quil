;; simple io abstraction layer for clojure
;; Roland Sadowski [szabla gmail com]

;; Copyright (c) 2008 Roland Sadowski. All rights reserved.  The use and
;; distribution terms for this software are covered by the Common
;; Public License 1.0 (http://www.opensource.org/licenses/cpl1.0.php)
;; which can be found in the file CPL.TXT at the root of this
;; distribution.  By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license.  You must not
;; remove this notice, or any other, from this software. 

(clojure/ns rosado.io
			(:import (java.io File FileReader FileWriter BufferedReader BufferedWriter
							   FileInputStream FileOutputStream FileOutputStream
							   BufferedInputStream BufferedOutputStream)))

(defn reader 
  "Returns a reader or input stream (buffered).
  Optional keywords: :unbuff [:ub] - for unbuffered input. 
  :bytes - for an InputStream instead of a Reader."
  [#^String file-name & modes]
  (cond
   (nil? modes) (BufferedReader. (FileReader. file-name))
   (some #{:bytes} modes) (if (some #{:unbuff :ub} modes)
							(FileInputStream. file-name)
							(BufferedInputStream. (FileInputStream. file-name)))
   (some #{:unbuff :ub} modes) (FileReader. file-name)))

(defn read-lines 
  "Returns a list of lines from seq."
  [#^java.io.BufferedReader rdr]
  (loop [lines [] line (.readLine rdr)]
	(if (nil? line)
	  lines
	  (recur (conj lines line) (.readLine rdr)))))

(defn writer
  "Returns a writer or output stream (buffered).
  Optional keywords: :unbuff [:ub] - for unbuffered output. 
  :bytes - for an OutputStream instead of a Writer."
  [#^String file-name & modes]
  (cond
   (nil? modes) (BufferedWriter. (FileWriter. file-name))
   (some #{:bytes} modes) (if (some #{:unbuff :ub} modes)
							(FileOutputStream. file-name)
							(BufferedOutputStream. (FileOutputStream. file-name)))
   (some #{:unbuff :ub} modes) (FileWriter. file-name)))

(defn write-lines 
  "Writes a seq of lines to a writer. Appends a newline to ecah line."
  [#^java.io.BufferedWriter a-writer lines]
  (loop [lns lines]
	(when lns
	  (.write a-writer (first lns))
	  (.newLine a-writer)
	  (recur (rest lns)))))
