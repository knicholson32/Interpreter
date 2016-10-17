BEGIN
  BEGIN
    number := 2;
    a := number;
    b := 10 * a + 10 * number div 40;
    c := a - - b;
    _d := (a+2)*3; {This is a test of comments}
    divel := _d;
  END;
  x := 11;
END.
