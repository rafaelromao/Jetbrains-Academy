partitioningBy((String w) ->  {
    for (var i = 0; i < w.length() / 2; i++) {
        if (w.charAt(i) != w.charAt(w.length() - i - 1)) {
            return false;
        }
    }
    return true;
})
