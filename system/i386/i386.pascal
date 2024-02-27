program RandomNumberGenerator;

{$APPTYPE CONSOLE}

uses
  SysUtils, Math;

function GenerateRandomNumber: Integer;
begin
  try
    Randomize; // Initialize random number generator
    Result := Random(MaxInt); // Generate a random number
  except
    on E: Exception do
      Writeln('Error generating random number: ', E.Message);
  end;
end;

begin
  try
    Writeln('Random Number Generated: ', GenerateRandomNumber);
  except
    on E: Exception do
      Writeln('An error occurred: ', E.Message);
  end;
end.
