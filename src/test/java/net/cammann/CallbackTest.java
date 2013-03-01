package net.cammann;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import net.cammann.annotations.Benchmark;
import net.cammann.annotations.Callback;
import net.cammann.callback.CallbackEvent;
import net.cammann.callback.CallbackListener;
import net.cammann.results.MethodResult;
import net.cammann.results.Result;

import org.junit.Test;

public class CallbackTest {

	@Callback("callback_one")
	private int callback;

	@Benchmark(10)
	public int getFieldCallback() {
		return callback;
	}

	@Benchmark(15)
	public String getParamCallback(@Callback("callback_two") String x) {
		return x;
	}

	@Test
	public void test() {
		Benchmarker.addCallback("callback_one", new CallbackListener<Integer>() {
			@Override
			public Integer callback(CallbackEvent event) {
				Integer out = event.getRun();
				return out;
			}
		});

		Benchmarker.addCallback("callback_two", new CallbackListener<String>() {
			@Override
			public String callback(CallbackEvent event) {
				String out = String.valueOf(event.getRun() * 5);
				System.out.println("callback - run: " + event.getRun() + " giving: " + out);
				return out;
			}
		});

		Result result = Benchmarker.run(CallbackTest.class);
		ParameterisedMethod pmOne = result.getParameterisedMethodsTested().get(0);
		ParameterisedMethod pmTwo = result.getParameterisedMethodsTested().get(1);
		if(pmOne.getMethod().getName().equals("getParamCallback")) {
			List<MethodResult> reusltList = result.getMethodResults(pmOne);
			for (int i = 0; i < 10; i++) {
				assertEquals(String.valueOf((i + 1) * 5), reusltList.get(i).getReturned().get());
			}

			reusltList = result.getMethodResults(pmTwo);
			for (int i = 0; i < 10; i++) {
				assertEquals(i + 1, reusltList.get(i).getReturned().get());
			}

		} else if (pmOne.getMethod().getName().equals("getFieldCallback")) {

			List<MethodResult> resultList = result.getMethodResults(pmOne);
			for (int i = 0; i < 10; i++) {
				assertEquals(i + 1, resultList.get(i).getReturned().get());
			}


			for (int i = 1; i < 11; i++) {
				String ans = String.valueOf(i * 5);
				resultList = result.getMethodResults(new ParameterisedMethod(pmTwo.getMethod(), new Object[]{ans}));
				String r = (String) resultList.get(0).getReturned().get();
				assertEquals(ans, r);
			}


		} else {
			fail("can't find correct method, instead got: " + pmOne);
		}



	}


}
