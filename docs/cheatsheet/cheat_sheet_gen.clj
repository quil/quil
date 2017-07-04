(ns docs.cheat-sheet-gen
  (:require [quil.helpers.docs :as docs]))

(def fns 'quil.core)
(require fns)
(def fn-metas (->> fns ns-publics vals (map meta)))

(def cheat-sheet-start
  (str
   "
\\documentclass[footexclude,twocolumn,DIV40,fontsize=8.7pt]{scrreprt}

% Author: Sam Aaron
% Comments, errors, suggestions: sam.aaron(at)gmail.com

% Based on Steve Tayon's excellent Clojure Cheat Sheet

% License
% Eclipse Public License v1.0
% http://opensource.org/licenses/eclipse-1.0.php

% Packages
\\usepackage[utf8]{inputenc}
\\usepackage[T1]{fontenc}
\\usepackage{textcomp}
\\usepackage[english]{babel}
\\usepackage{tabularx}
\\usepackage{lmodern}
\\renewcommand*\\familydefault{\\sfdefault}


\\usepackage[table]{xcolor}

% Set column space
\\setlength{\\columnsep}{2em}

% Define colours
\\definecolorset{hsb}{}{}{red,0,.4,0.95;orange,.1,.4,0.95;green,.25,.4,0.95;grey,0.0,0.0,0.1;yellow,.15,.4,0.95}

\\definecolorset{hsb}{}{}{blue,.55,.4,0.95;purple,.7,.4,0.95;pink,.8,.4,0.95;blue2,.58,.4,0.95}

\\definecolorset{hsb}{}{}{magenta,.9,.4,0.95;green2,.29,.4,0.95}

% Redefine sections
\\makeatletter
\\renewcommand{\\section}{\\@startsection{section}{1}{0mm}
  {-1.7ex}{0.7ex}{\\normalfont\\large\\bfseries}}
\\renewcommand{\\subsection}{\\@startsection{subsection}{2}{0mm}
  {-1.7ex}{0.5ex}{\\normalfont\\normalsize\\bfseries}}
\\makeatother

% No section numbers
\\setcounter{secnumdepth}{0}

% No indentation
\\setlength{\\parindent}{0em}

% No header and footer
\\pagestyle{empty}


% A few shortcuts
\\newcommand{\\cmd}[1] {\\frenchspacing\\texttt{\\textbf{{#1}}}}
\\newcommand{\\cmdline}[1] {
  \\begin{tabularx}{\\hsize}{X}
    \\texttt{\\textbf{{#1}}}
  \\end{tabularx}
}

\\newcommand{\\colouredbox}[2] {
  \\colorbox{#1!40}{
    \\begin{minipage}{\\linewidth}
      {
        \\rowcolors[]{1}{#1!20}{#1!10}
        #2
      }
    \\end{minipage}
  }
}


%%%%%%%% BEGIN DOC %%%%%%%%%%%%

\\begin{document}

 {\\Large{\\textbf{Quil " (System/getProperty "quil.version") "}}}

"))

(def cheat-sheet-applet
  "
\\colouredbox{blue}{
  \\section{Sketch options}
  \\begin{tabularx}{\\hsize}{lX}
 Config Keywords & \\cmd{:title :size :renderer :output-file :features :bgcolor :display :host :middleware} \\\\
 Draw Callback Keywords & \\cmd{:setup :draw :on-close :settings} \\\\
 Mouse Callback Keywords & \\cmd{:mouse-pressed :mouse-released :mouse-moved :mouse-dragged :mouse-entered :mouse-exited :mouse-clicked :mouse-wheel} \\\\
 Keyboard Callback Keywords & \\cmd{:key-pressed :key-released :key-typed } \\\\
 Window Callback Keywords & \\cmd{:focus-gained :focus-lost} \\\\
  \\end{tabularx}
}
")

(def cheat-sheet-docs
  "
\\colouredbox{grey}{
  \\section{Reflection}
  \\begin{tabularx}{\\hsize}{lX}
API Exploration & \\cmd{doc-cats doc-fns doc-meths} \\\\
  \\end{tabularx}
}

\\end{document}

")

(defn cheat-sheet-dynamic
  []
  (let [colours (cycle ["blue2" "yellow" "purple" "green" "orange" "pink" "blue" "yellow" "grey" "red" "green" "blue" "pink" "yellow"])
        info    (#'docs/sorted-category-map fn-metas)
        clean   (fn [s] (.replaceAll s "&" "\\\\&"))]
    (apply str
           (map (fn [[cat-idx cat-info] colour]
                  (str
                   "\\colouredbox{" colour "}{\n"
                   "  \\section{" (clean (:name cat-info)) "}\n"
                   "  \\begin{tabularx}{\\hsize}{lX}\n"
                   (when (> (count (:fns cat-info)) 0)
                     (str
                      "   & \\cmd{"
                      (apply str (map (fn [f] (str f " ")) (:fns cat-info)))
                      "} \\\\\n"))
                   (apply str
                          (map (fn [[subcat-idx {:keys [name fns]}]]
                                 (when (> (count fns) 0)
                                   (str
                                    "   " (clean name) " & \\cmd{"
                                    (apply str (map (fn [f] (str f " ")) fns))
                                    "} \\\\\n")))
                               (:subcategories cat-info)))
                   "  \\end{tabularx}\n"
                   "}\n"))
                info colours))))

(defn mk-cheat-sheet
  []
  (str
   cheat-sheet-start
   (cheat-sheet-dynamic)
   cheat-sheet-applet
   cheat-sheet-docs))

(spit "docs/cheatsheet/cheat-sheet.tex" (mk-cheat-sheet))
