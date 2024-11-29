/*

Not precausions gainst integer overflows -> in very fancy software that is dealing with large sums of money, it might be useufl to wrap integers into a class and then override arithmetic operators and do overflow checking in them, but since thi sprogram has no commercial value I skipped it.

The example lists costs in dollar signs, which could be USD, CAD, AUD, etc.
so idk. Maybe we will accept other currencies in the future? What kinds of
denomimnations will be supported?

For now let's just go with a couple of different dollar types and also the euros,
and a single "crypto" currency (using crypto currencies is supposed to attract investors).

Since the requirements , most used one is the United States dollar, so we will 
go with that as a default.

Replace unsafe string operations with safe string operations
maybe add std library containers (didn;t feel like using it toiday_)

*/

// TODO: add ethereum coin

#include <cstdio>
#include <cstdlib>
#include <cassert>
#include <cstdint>
#include <cstring>
#include <exception>

class InvalidCurrencyConversion : public std::exception {
public:
	const char* what() {
		return "Invalid currency conversion attempted.";
	}
};


class InvalidCurrencyComparison : public std::exception {
public:
	const char* what() {
		return "Invalid currency comparison attempted.";
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
	virtual bool operator<(const Currency* other) = 0;
};

enum DollarCurrencyType {
	CURRENCY_DOLLAR_UNITED_STATES,
	CURRENCY_DOLLAR_AUSTRALIA,
	CURRENCY_DOLLAR_CANADA,
	CURRENCY_DOLLAR_NEW_ZEALAND,
	CURRENCY_DOLLAR_ZIMBABWE
	// we can extend the list later I guess
};

class Euro : public Currency {
public:
	Euro(uint32_t euros, uint32_t cents) : euros(euros), cents(cents) {}
	
	uint32_t GetEuros() const { return euros; }
	uint32_t GetCents() const { return cents; }
	
	virtual bool operator<(const Currency* other) override {
		throw InvalidCurrencyComparison();
	}
	
private:
	uint32_t euros = 0;
	uint32_t cents = 0;
	
	// using integers, since integer math is more precise than floating-point.
	// also fancy financial sofware usually divides cents into fractions, but
	// this is not very fancy financial software.
};

class DollarCurrency : public Currency {
public:
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
	
	virtual bool operator<(const Currency* other) override {
		const DollarCurrency* other_dollar = dynamic_cast<const DollarCurrency*>(other);
		
		if (!other_dollar) {
			throw InvalidCurrencyComparison();
		}
		
		if (this->GetType() != other_dollar->GetType()) {
			throw InvalidCurrencyComparison();
		}
		
		if (this->GetDollars() != other_dollar->GetDollars()) {
			return this->GetDollars() < other_dollar->GetDollars();
		}
		
		return this->GetCents() < other_dollar->GetCents();
	}
	
private:
	DollarCurrencyType type = CURRENCY_DOLLAR_UNITED_STATES;
	
	uint32_t dollars = 0;
	uint32_t cents = 0;
};


class CurrencyFactory {
private:
	static uint32_t GetEuroToDollarRate(DollarCurrencyType type) {
		switch (type) {
			case CURRENCY_DOLLAR_UNITED_STATES: return 106;
			case CURRENCY_DOLLAR_AUSTRALIA:     return 162;
			case CURRENCY_DOLLAR_CANADA:        return 148;
			case CURRENCY_DOLLAR_NEW_ZEALAND:   return 179;
			case CURRENCY_DOLLAR_ZIMBABWE:      return 33953;
		}
	}
	
	static DollarCurrencyType default_currency;
public:

	static void SetDefaultCurrency(DollarCurrencyType type) {
		default_currency = type;
	}

	static Currency* CreateCurrencyHeuristic(const char* str) {
		throw CurrencyHeuristicFailure();
	}
	
	static DollarCurrency* ConvertEuroToDollar(Euro* euro,
		                                       DollarCurrencyType type) {
												   
		const uint32_t EURO_TO_DOLLAR_RATE = GetEuroToDollarRate(type);
		
		const uint32_t euro_cents = euro->GetEuros() + 100 * euro->GetCents();
		const uint32_t dollar_cents = (euro_cents * EURO_TO_DOLLAR_RATE) / 100;
		
		return new DollarCurrency(type, dollar_cents / 100, dollar_cents % 100);
	}
	
	static DollarCurrency* ConvertToDefaultCurrency(Currency* currency) {
		if (Euro* euro = dynamic_cast<Euro*>(currency); euro) {
			return ConvertEuroToDollar(euro, default_currency);
		} else if (DollarCurrency* dollar = dynamic_cast<DollarCurrency*>(currency); dollar) {
			
			// no dollar -> dollar conversion table yet, so let's just throw an
			// exception for now
			if (dollar->GetType() != default_currency) {
				throw InvalidCurrencyConversion();
			}
			
			return new DollarCurrency(*dollar);
		}
		
		throw InvalidCurrencyConversion();
	}
};

DollarCurrencyType CurrencyFactory::default_currency = CURRENCY_DOLLAR_UNITED_STATES;

class Stakeholder;

struct StakeholderApproval {
	Stakeholder* approver = nullptr;
	bool approved = false;
	
	static StakeholderApproval Approve(Stakeholder* approver) {
		return StakeholderApproval {
			.approver = approver,
			.approved = true
		};
	}
	
	static StakeholderApproval Deny() {
		return StakeholderApproval {
			.approver = nullptr,
			.approved = false
		};
	}
};

class Stakeholder {
public:
	virtual StakeholderApproval TryGetApproved(Currency* sum) = 0;
	virtual const char* GetName() = 0;
	virtual ~Stakeholder() = default;
};

/// Generic Stakeholder that doesn't really do anything special in particular.
/// You could hang a decorator on this to get new behaviors, but I don't know if
/// it will be needed.
/// @param name 	This name will be printed to console. Tbe string will be copied.
/// @param sum 		Ammount of currency which the stakeholder will approve.
/// @param delegate	An optional delegate. See Stakeholder.
class GenericStakeholder : public Stakeholder {
public:
	GenericStakeholder(const char* name, Currency* sum, Stakeholder* delegate = nullptr) {
		this->name = new char[strlen(name) + 1];
		strcpy(this->name, name);
		this->sum = sum;
		this->delegate = delegate;
	}
	
	StakeholderApproval TryGetApproved(Currency* sum) override {
		if (sum < this->sum) {
			return StakeholderApproval::Approve(this);
		}
		
		if (delegate) {
			delegate->TryGetApproved(sum);
		}
		
		return StakeholderApproval::Deny();
	}
	
	const char* GetName() override {
		return name;
	}
	
	~GenericStakeholder() override {
		if (name) delete name;
	}
private:
	char* name = nullptr;
	Currency* sum = nullptr;
	Stakeholder* delegate = nullptr;
};

/// CEO stakeholder.
/// CEO gets their own class, since CEOs are very important.
class CEO : public Stakeholder {
public:
	CEO(Stakeholder* delegate = nullptr) {
		this->delegate = delegate;
	}
	
	const char* GetName() override {
		return "CEO";
	}
	
	StakeholderApproval TryGetApproved(Currency* sum) override {
		DollarCurrency approval_sum(CURRENCY_DOLLAR_UNITED_STATES, 100'000, 0);

		if (sum < static_cast<Currency*>(&approval_sum)) {
			return StakeholderApproval::Approve(this);
		}
		
		if (delegate) {
			delegate->TryGetApproved(sum);
		}
		
		return StakeholderApproval::Deny();
	}
	
private:
	Stakeholder* delegate = nullptr;
};

class StakeholderFactory {
public:
	Stakeholder* DivisionDirector(Stakeholder* delegate = nullptr) {
		return new GenericStakeholder("Division Director",
	                                  new DollarCurrency(CURRENCY_DOLLAR_UNITED_STATES, 20'000, 0),
									  delegate);
	}
	
	Stakeholder* SubdivisionManager(Stakeholder* delegate = nullptr) {
		return new GenericStakeholder("Subdivision Manager",
	                                  new DollarCurrency(CURRENCY_DOLLAR_UNITED_STATES, 5'000, 0),
									  delegate);
	}
	
	Stakeholder* ProgramManager(Stakeholder* delegate = nullptr) {
		return new GenericStakeholder("Program Manager",
	                                  new DollarCurrency(CURRENCY_DOLLAR_UNITED_STATES, 2'000, 0),
									  delegate);
	}
	
	Stakeholder* ProjectManager(Stakeholder* delegate = nullptr) {
		return new GenericStakeholder("Project Manager",
	                                  new DollarCurrency(CURRENCY_DOLLAR_UNITED_STATES, 500, 0),
									  delegate);
	}
	
};

int main(int argc, const char** argv) {
	if (argc != 2) {
		printf("Usage: responsibility sum\n");
		printf("\tsum is the sum which will be checked for approval\n");
		printf("Accepted currencies:\n");
		printf("\t$, eur, E, Eth\n");
		printf("Example:\n");
		printf("\tresponsibility 400$");
	}
	
	Currency* check_currency = CurrencyFactory::CreateCurrencyHeuristic(argv[1]);
	
	
	Stakeholder* ceo = new CEO;
	Stakeholder* d_director = ;
	
	
	return 0;
}


/*

struct CurrencyAmmount {
 CurrencyType;
 int b
};

StakeholderApproval


Stakeholder*/
