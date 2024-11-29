/*

Not precausions gainst integer overflows -> in very fancy software that is dealing with large sums of money, it might be useufl to wrap integers into a class and then override arithmetic operators and do overflow checking in them, but since thi sprogram has no commercial value I skipped it.

The example lists costs in dollar signs, which could be USD, CAD, AUD, etc.
so idk. Maybe we will accept other currencies in the future? What kinds of
denomimnations will be supported?

For now let's just go with a couple of different dollar types and also the euros,
and a single "crypto" currency (using crypto currencies is supposed to attract investors).

Since the requirements , most used one is the United States dollar, so we will 
go with that as a default.


*/

#include <cstdio>
#include <cstdlib>
#include <cassert>
#include <cstdint>

class InvalidCurrencyConversion : public std::exception {
public:
	const char* what() {
		return "Invalid currency conversion attempted.";
	}
};

class CurrencyHeuristicFailure : public std::exception {
public:
	const char* what() {
		return "Currency determination heuristic failed.";
	}
};


// we'll need this to test the program
class CLITextPrinterInterface {
public:
	virtual void Print(const char*) = 0;
	// TODO: investigate whether we need a Print(wchar_t*) function
	//       - maybe somone wants to print currencies in UTF-16
	//       - idk, I only print CLI text in ASCII
	
	virtual ~CLITextPrinterInterface() = default;
};

class CLITextPrinter : public CLITextPrinterInterface {
public:
	void Print(const char* text) override {
		printf("%s\n", text);
	}
	
	virtual ~CLITextPrinter() = default;
};

class CLITextPrinterForTesting : public CLITextPrinterInterface {
public:
	CLITextPrinterForTesting(size_t size) {
		this->buffer = new char*[size];
		this->buffer_total_size = size;
		
		this->buffer_size = 0;
	}
	
	void Print(const char* text) override {
		printf("CLI Print: %s\n", text);
		
		if (buffer_size < buffer_total_size) {
			printf("CLI Text Printer buffer overflow; test canceled\n", text);
			abort();
		}
	}
	
	const char* GetBufferEntry(size_t index) const {
		assert(index < buffer_size);
		
		return buffer[index];
	}
	
	size_t GetBufferSize() const {
		return buffer_size;
	}
	
	void ResetBuffer() {
		for (size_t i = 0; i < buffer_size; i++) {
			delete buffer[i];
		}
		
		buffer_size = 0;
	}
	
	~CLITextPrinterForTesting() override {
		ResetBuffer();
		delete buffer;
	}
	
private:
	size_t buffer_total_size = 0;
	size_t buffer_size = 0;
	char** buffer = nullptr;
};


class Currency {
	virtual size_t PrintToString(char* str, size_t str_len);
};

enum DollarCurrencyType {
	CURRENCY_DOLLAR_UNITED_STATES,
	CURRENCY_DOLLAR_AUSTRALIA,
	CURRENCY_DOLLAR_CANADA,
	CURRENCY_DOLLAR_NEW_ZEALAND,
	CURRENCY_DOLLAR_ZIMBABWE
	// we can extend the list later I guess
};

class Euro {
public:
	Euro(uint32_t euros, uint32_t cents) : euros(euros), cents(cents) {}
	
	uint32_t GetEuros() const { return euros; }
	uint32_t GetCents() const { return cents; }
private:
	uint32_t euros = 0;
	uint32_t cents = 0;
	
	// using integers, since integer math is more precise than floating-point.
	// also fancy financial sofware usually divides cents into fractions, but
	// this is not very fancy financial software.
};

class DollarCurrency {
	DollarCurrency(DollarCurrencyType, uint32_t dollars, uint32_t cents) {
		this->type = type;
		this->dollars = dollars;
		this->cents = cents;
	}
	
	DollarCurrency(const DollarCurrency& other) {
		this->type = other.type;
		this->dollars = other.dollars;
		this->cents = other.cents;
	}
	
	DollarCurrencyType GetType() const { return type; }
	uint32_t GetDollars() const { return dollars; }
	uint32_t GetCents() const { return cents; }
	
private:
	DollarCurrencyType type = CURRENCY_DOLLAR_UNITED_STATES;
	
	uint32_t dollars = 0;
	uint32_t cents = 0;
};


class CurrencyFactory {
private:
	uint32_t GetEuroToDollarRate(DollarCurrencyType type) {
		switch (type) {
			case CURRENCY_DOLLAR_UNITED_STATES: return 106;
			case CURRENCY_DOLLAR_AUSTRALIA:     return 162;
			case CURRENCY_DOLLAR_CANADA:        return 148;
			case CURRENCY_DOLLAR_NEW_ZEALAND:   return 179;
			case CURRENCY_DOLLAR_ZIMBABWE:      return 33953;
		}
	}
	
	DollarCurrencyType default_currency = CURRENCY_DOLLAR_UNITED_STATES;
public:

	static SetDefaultCurrency(DollarCurrencyType type) {
		default_currency = type;
	}

	static Currency* CreateCurrencyHeuristic(const char* str) {
		throw CurrencyHeuristicFailure;
	}
	
	static DollarCurrency* ConvertEuroToDollar(Euro* euro,
		                                       DollarCurrencyType type) {
												   
		const uint32_t EURO_TO_DOLLAR_RATE = GetEuroToDollarRate(type);
		
		const uint32_t euro_cents = euro->GetEuros() + 100 * euro->GetCents();
		const uint32_t dollar_cents = (euro_cents * EURO_TO_DOLLAR_RATE) / 100;
		
		return new DollarCurrency(dollar_cents / 100, dollar_cents % 100)
	}
	
	static Currency* ConvertToDefaultCurrency(Currency* currency) {
		if (Euro* euro = dynamic_cast<Euro*>(currency); euro) {
			return new DollarCurrency(euro, default_currency);
		} else if (DollarCurrency* dollar = dynamic_cast<DollarCurrency*>(currency); dollar) {
			
			// no dollar -> dollar conversion table yet, so let's just throw an
			// exception for now
			if (dollar->GetType() != default_currency) {
				throw InvalidCurrencyConversion;
			}
			
			return new DollarCurrency(dollar);
		}
		
		throw InvalidCurrencyConversion;
	}
};

struct StakeholderApproval {
	Stakeholder* approver = nullptr;
	bool approved = false;
	
	StakeholderApproval Approved(Stakeholder* approver) {
		return StakeholderApproval {
			.approver = approver,
			.approved = true
		};
	}
	
	StakeholderApproval Denied() {
		return StakeholderApproval {
			.approver = nullptr,
			.approved = false
		};
	}
};

/*

struct CurrencyAmmount {
 CurrencyType;
 int b
};

StakeholderApproval


Stakeholder*/
